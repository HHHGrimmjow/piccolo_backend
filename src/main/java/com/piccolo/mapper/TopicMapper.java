package com.piccolo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piccolo.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {

    @Select("SELECT t.*, u.nickname as creator_name, u.avatar as creator_avatar, " +
            "(SELECT COUNT(*) FROM comment c WHERE c.topic_id = t.id AND c.deleted = 0) as comment_count, " +
            "(SELECT COUNT(*) FROM topic_option o WHERE o.topic_id = t.id) as option_count " +
            "FROM topic t LEFT JOIN user u ON t.creator_id = u.id " +
            "WHERE t.deleted = 0 AND t.status = #{status} ORDER BY t.created_at DESC")
    List<Map<String, Object>> selectTopicList(Integer status);

    @Update("UPDATE topic SET vote_count = vote_count + 1 WHERE id = #{topicId}")
    void incrementVoteCount(Long topicId);

    @Update("UPDATE topic SET view_count = view_count + 1 WHERE id = #{topicId}")
    void incrementViewCount(Long topicId);
}
