package com.piccolo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piccolo.entity.TopicOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TopicOptionMapper extends BaseMapper<TopicOption> {

    @Update("UPDATE topic_option SET vote_count = vote_count + 1 WHERE id = #{optionId}")
    void incrementVoteCount(Long optionId);
}
