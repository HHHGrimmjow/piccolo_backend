package com.piccolo.common;

/**
 * 常量定义
 */
public class Constants {

    /** 话题状态：进行中 */
    public static final int TOPIC_ACTIVE = 1;
    /** 话题状态：已结束 */
    public static final int TOPIC_ENDED = 2;
    /** 话题状态：已关闭 */
    public static final int TOPIC_CLOSED = 3;

    /** 默认头像 */
    public static final String DEFAULT_AVATAR = "https://api.dicebear.com/7.0/pixel-art/svg?seed=piccolo";

    /** 排序方式 */
    public static final String SORT_LATEST = "latest";
    public static final String SORT_HOT = "hot";
    public static final String SORT_VOTES = "votes";
}
