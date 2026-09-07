package com.piccolo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论最长500个字符")
    private String content;

    /** 父评论ID，0或不传为顶级评论 */
    private Long parentId;

    /** 回复的目标用户ID */
    private Long replyToUserId;
}
