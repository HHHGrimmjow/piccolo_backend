package com.piccolo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("vote_record")
public class VoteRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long topicId;

    private Long optionId;

    private LocalDateTime createdAt;
}
