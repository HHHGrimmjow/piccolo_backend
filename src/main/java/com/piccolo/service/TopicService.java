package com.piccolo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piccolo.dto.TopicDTO;
import com.piccolo.vo.TopicDetailVO;
import com.piccolo.vo.TopicVO;

public interface TopicService {

    Page<TopicVO> getTopicList(int page, int size, String sort, String keyword);

    TopicDetailVO getTopicDetail(Long topicId, Long currentUserId);

    Long createTopic(Long userId, TopicDTO dto);

    void closeTopic(Long userId, Long topicId);
}
