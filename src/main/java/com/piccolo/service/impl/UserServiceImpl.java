package com.piccolo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piccolo.common.BusinessException;
import com.piccolo.common.Constants;
import com.piccolo.dto.LoginDTO;
import com.piccolo.dto.RegisterDTO;
import com.piccolo.entity.User;
import com.piccolo.mapper.CommentMapper;
import com.piccolo.mapper.TopicMapper;
import com.piccolo.mapper.VoteRecordMapper;
import com.piccolo.mapper.UserMapper;
import com.piccolo.service.UserService;
import com.piccolo.util.JwtUtil;
import com.piccolo.vo.LoginVO;
import com.piccolo.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final TopicMapper topicMapper;
    private final VoteRecordMapper voteRecordMapper;
    private final CommentMapper commentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名不存在哦~");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("密码不对哦，再想想？");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUser(toUserVO(user));
        return vo;
    }

    @Override
    public void register(RegisterDTO dto) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("这个用户名已经被占用了~");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setAvatar(Constants.DEFAULT_AVATAR);
        user.setBio("");
        userMapper.insert(user);
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toUserVO(user);
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toUserVO(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void updateProfile(Long userId, String nickname, String avatar, String bio) {
        User user = new User();
        user.setId(userId);
        if (StringUtils.hasText(nickname)) user.setNickname(nickname);
        if (StringUtils.hasText(avatar)) user.setAvatar(avatar);
        if (bio != null) user.setBio(bio);
        userMapper.updateById(user);
    }

    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setBio(user.getBio());
        vo.setCreatedAt(user.getCreatedAt());

        // 统计数据
        vo.setTopicCount(Math.toIntExact(topicMapper.selectCount(
                new LambdaQueryWrapper<>().eq("creator_id", user.getId()))));
        vo.setVoteCount(Math.toIntExact(voteRecordMapper.selectCount(
                new LambdaQueryWrapper<>().eq("user_id", user.getId()))));
        vo.setCommentCount(Math.toIntExact(commentMapper.selectCount(
                new LambdaQueryWrapper<>().eq("user_id", user.getId()))));
        return vo;
    }
}
