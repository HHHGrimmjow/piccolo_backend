package com.piccolo.controller;

import com.piccolo.common.Result;
import com.piccolo.dto.VoteDTO;
import com.piccolo.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    /** 投票 */
    @PostMapping("/{id}/vote")
    public Result<?> vote(@PathVariable Long id,
                          @Valid @RequestBody VoteDTO dto,
                          Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        voteService.vote(userId, id, dto.getOptionId());
        return Result.success("投票成功~", null);
    }

    /** 投票历史 */
    @GetMapping("/my/history")
    public Result<List<Map<String, Object>>> voteHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(voteService.getVoteHistory(userId));
    }
}
