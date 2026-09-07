package com.piccolo.controller;

import com.piccolo.common.Result;
import com.piccolo.dto.CommentDTO;
import com.piccolo.service.CommentService;
import com.piccolo.vo.CommentVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics/{id}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /** 获取评论列表 */
    @GetMapping
    public Result<List<CommentVO>> list(@PathVariable Long id) {
        return Result.success(commentService.getComments(id));
    }

    /** 发表评论 */
    @PostMapping
    public Result<?> add(@PathVariable Long id,
                         @Valid @RequestBody CommentDTO dto,
                         Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        commentService.addComment(userId, id, dto);
        return Result.success("评论成功~", null);
    }
}
