package com.piccolo.controller;

import com.piccolo.common.Result;
import com.piccolo.service.UserService;
import com.piccolo.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 获取用户信息 */
    @GetMapping("/{id}")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    /** 更新个人信息 */
    @PutMapping("/me")
    public Result<?> updateProfile(@RequestBody Map<String, String> body,
                                   Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.updateProfile(userId,
                body.get("nickname"),
                body.get("avatar"),
                body.get("bio"));
        return Result.success("更新成功~", null);
    }
}
