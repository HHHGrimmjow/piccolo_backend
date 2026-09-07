package com.piccolo.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String bio;
    private LocalDateTime createdAt;

    // 统计数据
    private Integer topicCount;
    private Integer voteCount;
    private Integer commentCount;
}
