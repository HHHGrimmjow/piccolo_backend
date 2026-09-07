package com.piccolo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("topic_option")
public class TopicOption {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long topicId;

    private String optionText;

    private String optionImage;

    private Integer voteCount;

    private Integer sortOrder;
}
