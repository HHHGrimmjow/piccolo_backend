package com.piccolo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piccolo.common.Result;
import com.piccolo.dto.TopicDTO;
import com.piccolo.service.TopicService;
import com.piccolo.service.VoteService;
import com.piccolo.vo.TopicDetailVO;
import com.piccolo.vo.TopicVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /** 话题列表（分页） */
    @GetMapping
    public Result<Page<TopicVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String keyword) {
        return Result.success(topicService.getTopicList(page, size, sort, keyword));
    }

    /** 话题详情 */
    @GetMapping("/{id}/detail")
    public Result<TopicDetailVO> detail(@PathVariable Long id, Authentication authentication) {
        Long userId = authentication != null ? (Long) authentication.getPrincipal() : null;
        return Result.success(topicService.getTopicDetail(id, userId));
    }

    /** 创建话题 */
    @PostMapping
    public Result<Long> create(@Valid @RequestBody TopicDTO dto, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success("发布成功~", topicService.createTopic(userId, dto));
    }

    /** 关闭话题 */
    @PostMapping("/{id}/close")
    public Result<?> close(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        topicService.closeTopic(userId, id);
        return Result.success("已关闭~", null);
    }
}
