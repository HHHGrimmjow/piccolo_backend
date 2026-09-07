package com.piccolo.service;

import com.piccolo.dto.CommentDTO;
import com.piccolo.vo.CommentVO;

import java.util.List;

public interface CommentService {

    List<CommentVO> getComments(Long topicId);

    void addComment(Long userId, Long topicId, CommentDTO dto);
}
