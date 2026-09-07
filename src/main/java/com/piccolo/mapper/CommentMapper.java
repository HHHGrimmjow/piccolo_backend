package com.piccolo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piccolo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.nickname as username, u.avatar as user_avatar, " +
            "ru.nickname as reply_to_username " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN user ru ON c.reply_to_user_id = ru.id " +
            "WHERE c.topic_id = #{topicId} AND c.deleted = 0 " +
            "ORDER BY c.created_at ASC")
    List<Map<String, Object>> selectCommentsByTopicId(Long topicId);
}
