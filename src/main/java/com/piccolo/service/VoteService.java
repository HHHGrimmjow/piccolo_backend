package com.piccolo.service;

import java.util.List;
import java.util.Map;

public interface VoteService {

    void vote(Long userId, Long topicId, Long optionId);

    List<Map<String, Object>> getVoteHistory(Long userId);
}
