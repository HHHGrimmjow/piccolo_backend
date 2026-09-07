package com.piccolo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("topic")
public class Topic {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Long creatorId;

    private LocalDateTime deadline;

    /** 1-进行中 2-已结束 3-已关闭 */
    private Integer status;

    private Integer viewCount;

    private Integer voteCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
