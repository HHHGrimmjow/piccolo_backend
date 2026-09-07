package com.piccolo.service.impl;

import com.piccolo.common.BusinessException;
import com.piccolo.dto.CommentDTO;
import com.piccolo.entity.Comment;
import com.piccolo.mapper.CommentMapper;
import com.piccolo.service.CommentService;
import com.piccolo.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public List<CommentVO> getComments(Long topicId) {
        List<Map<String, Object>> rawComments = commentMapper.selectCommentsByTopicId(topicId);

        List<CommentVO> allComments = rawComments.stream().map(row -> {
            CommentVO vo = new CommentVO();
            vo.setId(((Number) row.get("id")).longValue());
            vo.setUserId(((Number) row.get("user_id")).longValue());
            vo.setUsername((String) row.get("username"));
            vo.setUserAvatar((String) row.get("user_avatar"));
            vo.setContent((String) row.get("content"));
            vo.setParentId(((Number) row.get("parent_id")).longValue());
            if (row.get("reply_to_user_id") != null) {
                vo.setReplyToUserId(((Number) row.get("reply_to_user_id")).longValue());
            }
            vo.setReplyToUsername((String) row.get("reply_to_username"));
            Object createdAt = row.get("created_at");
            if (createdAt instanceof LocalDateTime) {
                vo.setCreatedAt((LocalDateTime) createdAt);
            }
            vo.setChildren(new ArrayList<>());
            return vo;
        }).collect(Collectors.toList());

        // 构建树形结构
        Map<Long, CommentVO> commentMap = new LinkedHashMap<>();
        allComments.forEach(c -> commentMap.put(c.getId(), c));

        List<CommentVO> rootComments = new ArrayList<>();
        for (CommentVO comment : allComments) {
            if (comment.getParentId() == null || comment.getParentId() == 0) {
                rootComments.add(comment);
            } else {
                CommentVO parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment);
                } else {
                    rootComments.add(comment);
                }
            }
        }

        return rootComments;
    }

    @Override
    public void addComment(Long userId, Long topicId, CommentDTO dto) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setTopicId(topicId);
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        comment.setReplyToUserId(dto.getReplyToUserId());
        commentMapper.insert(comment);
    }
}
