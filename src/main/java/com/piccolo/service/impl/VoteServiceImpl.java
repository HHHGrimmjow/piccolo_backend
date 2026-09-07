package com.piccolo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piccolo.common.BusinessException;
import com.piccolo.common.Constants;
import com.piccolo.entity.Topic;
import com.piccolo.entity.TopicOption;
import com.piccolo.entity.VoteRecord;
import com.piccolo.mapper.TopicMapper;
import com.piccolo.mapper.TopicOptionMapper;
import com.piccolo.mapper.VoteRecordMapper;
import com.piccolo.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRecordMapper voteRecordMapper;
    private final TopicMapper topicMapper;
    private final TopicOptionMapper topicOptionMapper;

    @Override
    @Transactional
    public void vote(Long userId, Long topicId, Long optionId) {
        // 检查话题是否存在且有效
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null) {
            throw new BusinessException("话题不存在哦~");
        }
        if (topic.getStatus() != Constants.TOPIC_ACTIVE) {
            throw new BusinessException("这个话题已经结束了~");
        }

        // 检查是否已投票
        Long count = voteRecordMapper.selectCount(
                new LambdaQueryWrapper<VoteRecord>()
                        .eq(VoteRecord::getUserId, userId)
                        .eq(VoteRecord::getTopicId, topicId));
        if (count > 0) {
            throw new BusinessException("你已经投过票啦~");
        }

        // 检查选项是否属于该话题
        TopicOption option = topicOptionMapper.selectById(optionId);
        if (option == null || !option.getTopicId().equals(topicId)) {
            throw new BusinessException("选项无效哦~");
        }

        // 记录投票
        VoteRecord record = new VoteRecord();
        record.setUserId(userId);
        record.setTopicId(topicId);
        record.setOptionId(optionId);
        voteRecordMapper.insert(record);

        // 更新选项投票数
        topicOptionMapper.incrementVoteCount(optionId);

        // 更新话题投票数
        topicMapper.incrementVoteCount(topicId);
    }

    @Override
    public List<Map<String, Object>> getVoteHistory(Long userId) {
        return voteRecordMapper.selectVoteHistory(userId);
    }
}
