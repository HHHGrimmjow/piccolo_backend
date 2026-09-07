package com.piccolo.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TopicDetailVO {
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
    private LocalDateTime createdAt;

    /** 当前用户是否已投票 */
    private Boolean voted;

    /** 当前用户投票的选项ID */
    private Long votedOptionId;

    /** 投票选项列表 */
    private List<OptionVO> options;

    @Data
    public static class OptionVO {
        private Long id;
        private String optionText;
        private String optionImage;
        private Integer voteCount;
        private Integer sortOrder;
        /** 百分比（投票后显示） */
        private Double percentage;
    }
}
