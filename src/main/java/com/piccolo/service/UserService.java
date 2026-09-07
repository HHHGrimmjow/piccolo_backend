package com.piccolo.service;

import com.piccolo.dto.LoginDTO;
import com.piccolo.dto.RegisterDTO;
import com.piccolo.entity.User;
import com.piccolo.vo.LoginVO;
import com.piccolo.vo.UserVO;

public interface UserService {

    LoginVO login(LoginDTO dto);

    void register(RegisterDTO dto);

    UserVO getCurrentUser(Long userId);

    UserVO getUserById(Long userId);

    User getById(Long id);

    void updateProfile(Long userId, String nickname, String avatar, String bio);
}
