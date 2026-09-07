package com.piccolo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long topicId;

    private String content;

    /** 父评论ID，0为顶级评论 */
    private Long parentId;

    /** 回复的目标用户 */
    private Long replyToUserId;

    private LocalDateTime createdAt;

    @TableLogic
    private Integer deleted;
}
