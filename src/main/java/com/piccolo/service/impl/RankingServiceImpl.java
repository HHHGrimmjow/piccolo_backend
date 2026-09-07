package com.piccolo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piccolo.entity.Topic;
import com.piccolo.entity.User;
import com.piccolo.mapper.TopicMapper;
import com.piccolo.mapper.UserMapper;
import com.piccolo.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final TopicMapper topicMapper;
    private final UserMapper userMapper;

    @Override
    public Map<String, Object> getRankings() {
        Map<String, Object> result = new HashMap<>();

        // 热门话题 TOP 10
        List<Topic> hotTopics = topicMapper.selectList(
                new LambdaQueryWrapper<Topic>()
                        .eq(Topic::getStatus, 1)
                        .orderByDesc(Topic::getVoteCount)
                        .last("LIMIT 10"));

        List<Map<String, Object>> hotTopicList = hotTopics.stream().map(topic -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", topic.getId());
            map.put("title", topic.getTitle());
            map.put("voteCount", topic.getVoteCount());
            map.put("viewCount", topic.getViewCount());
            User creator = userMapper.selectById(topic.getCreatorId());
            map.put("creatorName", creator != null ? creator.getNickname() : "匿名");
            return map;
        }).collect(Collectors.toList());

        // 活跃用户 TOP 10（按创建话题数排序）
        List<User> allUsers = userMapper.selectList(new LambdaQueryWrapper<>());
        allUsers.sort((a, b) -> {
            long countA = topicMapper.selectCount(
                    new LambdaQueryWrapper<Topic>().eq(Topic::getCreatorId, a.getId()));
            long countB = topicMapper.selectCount(
                    new LambdaQueryWrapper<Topic>().eq(Topic::getCreatorId, b.getId()));
            return Long.compare(countB, countA);
        });

        List<Map<String, Object>> activeUserList = allUsers.stream()
                .limit(10)
                .map(user -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", user.getId());
                    map.put("nickname", user.getNickname());
                    map.put("avatar", user.getAvatar());
                    long topicCount = topicMapper.selectCount(
                            new LambdaQueryWrapper<Topic>().eq(Topic::getCreatorId, user.getId()));
                    map.put("topicCount", topicCount);
                    return map;
                }).collect(Collectors.toList());

        result.put("hotTopics", hotTopicList);
        result.put("activeUsers", activeUserList);
        return result;
    }
}
