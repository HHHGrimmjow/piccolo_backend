-- Piccolo Database Schema
-- 帮你做选择，投票更有趣 🎲

CREATE DATABASE IF NOT EXISTS piccolo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE piccolo;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(50) DEFAULT '',
    `avatar` VARCHAR(500) DEFAULT '',
    `bio` VARCHAR(200) DEFAULT '',
    `birthday` DATE DEFAULT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 投票话题表
CREATE TABLE IF NOT EXISTS `topic` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `image_url` VARCHAR(500) DEFAULT '',
    `creator_id` BIGINT NOT NULL,
    `deadline` DATETIME DEFAULT NULL,
    `status` TINYINT DEFAULT 1 COMMENT '1-进行中 2-已结束 3-已关闭',
    `view_count` INT DEFAULT 0,
    `vote_count` INT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_creator` (`creator_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票话题表';

-- 投票选项表
CREATE TABLE IF NOT EXISTS `topic_option` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `topic_id` BIGINT NOT NULL,
    `option_text` VARCHAR(200) NOT NULL,
    `option_image` VARCHAR(500) DEFAULT '',
    `vote_count` INT DEFAULT 0,
    `sort_order` INT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_topic` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票选项表';

-- 投票记录表
CREATE TABLE IF NOT EXISTS `vote_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `topic_id` BIGINT NOT NULL,
    `option_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_topic` (`user_id`, `topic_id`),
    KEY `idx_topic` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票记录表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `topic_id` BIGINT NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID，0为顶级评论',
    `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复的目标用户',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_topic` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';
