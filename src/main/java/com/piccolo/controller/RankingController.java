package com.piccolo.controller;

import com.piccolo.common.Result;
import com.piccolo.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    /** 获取排行榜 */
    @GetMapping
    public Result<Map<String, Object>> rankings() {
        return Result.success(rankingService.getRankings());
    }
}
