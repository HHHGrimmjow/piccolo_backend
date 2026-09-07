package com.piccolo.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {
    private Long id;
    private Long userId;
    private String username;
    private String userAvatar;
    private String content;
    private Long parentId;
    private Long replyToUserId;
    private String replyToUsername;
    private LocalDateTime createdAt;

    /** 子评论列表 */
    private List<CommentVO> children;
}
