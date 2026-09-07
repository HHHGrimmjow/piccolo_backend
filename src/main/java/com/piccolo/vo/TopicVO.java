package com.piccolo.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TopicVO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long creatorId;
    private String creatorName;
    private String creatorAvatar;
    private LocalDateTime deadline;
    private Integer status;
    private Integer viewCount;
    private Integer voteCount;
    private Integer commentCount;
    private Integer optionCount;
    private LocalDateTime createdAt;
}
