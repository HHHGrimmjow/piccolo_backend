package com.piccolo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piccolo.common.BusinessException;
import com.piccolo.common.Constants;
import com.piccolo.dto.TopicDTO;
import com.piccolo.entity.Comment;
import com.piccolo.entity.Topic;
import com.piccolo.entity.TopicOption;
import com.piccolo.entity.User;
import com.piccolo.entity.VoteRecord;
import com.piccolo.mapper.CommentMapper;
import com.piccolo.mapper.TopicMapper;
import com.piccolo.mapper.TopicOptionMapper;
import com.piccolo.mapper.UserMapper;
import com.piccolo.mapper.VoteRecordMapper;
import com.piccolo.service.TopicService;
import com.piccolo.vo.TopicDetailVO;
import com.piccolo.vo.TopicVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;
    private final TopicOptionMapper topicOptionMapper;
    private final UserMapper userMapper;
    private final VoteRecordMapper voteRecordMapper;
    private final CommentMapper commentMapper;

    @Override
    public Page<TopicVO> getTopicList(int page, int size, String sort, String keyword) {
        Page<Topic> topicPage = new Page<>(page, size);

        LambdaQueryWrapper<Topic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Topic::getStatus, Constants.TOPIC_ACTIVE);

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Topic::getTitle, keyword)
                    .or().like(Topic::getDescription, keyword));
        }

        // 排序
        if (Constants.SORT_HOT.equals(sort)) {
            wrapper.orderByDesc(Topic::getVoteCount);
        } else if (Constants.SORT_VOTES.equals(sort)) {
            wrapper.orderByDesc(Topic::getVoteCount);
        } else {
            wrapper.orderByDesc(Topic::getCreatedAt);
        }

        Page<Topic> result = topicMapper.selectPage(topicPage, wrapper);

        // 转换为VO
        Page<TopicVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<TopicVO> voList = new ArrayList<>();
        for (Topic topic : result.getRecords()) {
            voList.add(toTopicVO(topic));
        }
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public TopicDetailVO getTopicDetail(Long topicId, Long currentUserId) {
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null) {
            throw new BusinessException("话题不存在哦~");
        }

        // 增加浏览量
        topicMapper.incrementViewCount(topicId);

        User creator = userMapper.selectById(topic.getCreatorId());
        List<TopicOption> options = topicOptionMapper.selectList(
                new LambdaQueryWrapper<TopicOption>()
                        .eq(TopicOption::getTopicId, topicId)
                        .orderByAsc(TopicOption::getSortOrder));

        // 检查当前用户是否已投票
        Boolean voted = false;
        Long votedOptionId = null;
        if (currentUserId != null) {
            VoteRecord record = voteRecordMapper.selectOne(
                    new LambdaQueryWrapper<VoteRecord>()
                            .eq(VoteRecord::getUserId, currentUserId)
                            .eq(VoteRecord::getTopicId, topicId));
            if (record != null) {
                voted = true;
                votedOptionId = record.getOptionId();
            }
        }

        // 计算总投票数
        int totalVotes = options.stream().mapToInt(TopicOption::getVoteCount).sum();

        // 构建VO
        TopicDetailVO vo = new TopicDetailVO();
        vo.setId(topic.getId());
        vo.setTitle(topic.getTitle());
        vo.setDescription(topic.getDescription());
        vo.setImageUrl(topic.getImageUrl());
        vo.setCreatorId(topic.getCreatorId());
        vo.setCreatorName(creator != null ? creator.getNickname() : "匿名");
        vo.setCreatorAvatar(creator != null ? creator.getAvatar() : "");
        vo.setDeadline(topic.getDeadline());
        vo.setStatus(topic.getStatus());
        vo.setViewCount(topic.getViewCount() + 1); // +1 for this view
        vo.setVoteCount(topic.getVoteCount());
        vo.setCreatedAt(topic.getCreatedAt());
        vo.setVoted(voted);
        vo.setVotedOptionId(votedOptionId);

        List<TopicDetailVO.OptionVO> optionVOs = new ArrayList<>();
        for (TopicOption option : options) {
            TopicDetailVO.OptionVO optionVO = new TopicDetailVO.OptionVO();
            optionVO.setId(option.getId());
            optionVO.setOptionText(option.getOptionText());
            optionVO.setOptionImage(option.getOptionImage());
            optionVO.setVoteCount(option.getVoteCount());
            optionVO.setSortOrder(option.getSortOrder());
            optionVO.setPercentage(totalVotes > 0
                    ? Math.round(option.getVoteCount() * 1000.0 / totalVotes) / 10.0
                    : 0.0);
            optionVOs.add(optionVO);
        }
        vo.setOptions(optionVOs);

        return vo;
    }

    @Override
    @Transactional
    public Long createTopic(Long userId, TopicDTO dto) {
        // 创建话题
        Topic topic = new Topic();
        topic.setTitle(dto.getTitle());
        topic.setDescription(dto.getDescription());
        topic.setImageUrl(dto.getImageUrl() != null ? dto.getImageUrl() : "");
        topic.setCreatorId(userId);
        topic.setDeadline(dto.getDeadline());
        topic.setStatus(Constants.TOPIC_ACTIVE);
        topic.setViewCount(0);
        topic.setVoteCount(0);
        topicMapper.insert(topic);

        // 创建选项
        List<TopicDTO.OptionDTO> options = dto.getOptions();
        for (int i = 0; i < options.size(); i++) {
            TopicOption option = new TopicOption();
            option.setTopicId(topic.getId());
            option.setOptionText(options.get(i).getOptionText());
            option.setOptionImage(options.get(i).getOptionImage() != null ? options.get(i).getOptionImage() : "");
            option.setVoteCount(0);
            option.setSortOrder(i);
            topicOptionMapper.insert(option);
        }

        return topic.getId();
    }

    @Override
    public void closeTopic(Long userId, Long topicId) {
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null) {
            throw new BusinessException("话题不存在");
        }
        if (!topic.getCreatorId().equals(userId)) {
            throw new BusinessException("只能关闭自己创建的话题哦~");
        }
        topic.setStatus(Constants.TOPIC_CLOSED);
        topicMapper.updateById(topic);
    }

    private TopicVO toTopicVO(Topic topic) {
        TopicVO vo = new TopicVO();
        vo.setId(topic.getId());
        vo.setTitle(topic.getTitle());
        vo.setDescription(topic.getDescription());
        vo.setImageUrl(topic.getImageUrl());
        vo.setCreatorId(topic.getCreatorId());
        vo.setDeadline(topic.getDeadline());
        vo.setStatus(topic.getStatus());
        vo.setViewCount(topic.getViewCount());
        vo.setVoteCount(topic.getVoteCount());
        vo.setCreatedAt(topic.getCreatedAt());

        // 创建者信息
        User creator = userMapper.selectById(topic.getCreatorId());
        if (creator != null) {
            vo.setCreatorName(creator.getNickname());
            vo.setCreatorAvatar(creator.getAvatar());
        }

        // 评论数和选项数
        vo.setCommentCount(Math.toIntExact(commentMapper.selectCount(
                new QueryWrapper<Comment>().eq("topic_id", topic.getId()))));
        vo.setOptionCount(Math.toIntExact(topicOptionMapper.selectCount(
                new QueryWrapper<TopicOption>().eq("topic_id", topic.getId()))));

        return vo;
    }
}
