package com.piccolo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piccolo.entity.VoteRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface VoteRecordMapper extends BaseMapper<VoteRecord> {

    @Select("SELECT vr.*, t.title as topic_title, t.image_url as topic_image, " +
            "o.option_text, o.id as option_id " +
            "FROM vote_record vr " +
            "LEFT JOIN topic t ON vr.topic_id = t.id " +
            "LEFT JOIN topic_option o ON vr.option_id = o.id " +
            "WHERE vr.user_id = #{userId} ORDER BY vr.created_at DESC")
    List<Map<String, Object>> selectVoteHistory(Long userId);
}
