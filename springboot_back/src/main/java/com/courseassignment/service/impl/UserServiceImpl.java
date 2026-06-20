package com.courseassignment.service.impl;

import com.courseassignment.config.JwtUtil;
import com.courseassignment.dto.LoginRequest;
import com.courseassignment.dto.LoginResponse;
import com.courseassignment.dto.RegisterRequest;
import com.courseassignment.entity.User;
import com.courseassignment.mapper.UserMapper;
import com.courseassignment.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRealName(), user.getRole());
    }

    @Override
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查学号/工号是否已存在
        if (request.getStudentNo() != null) {
            User existingStudent = userMapper.findByStudentNo(request.getStudentNo());
            if (existingStudent != null) {
                throw new RuntimeException("学号已存在");
            }
        }
        if (request.getTeacherNo() != null) {
            User existingTeacher = userMapper.findByTeacherNo(request.getTeacherNo());
            if (existingTeacher != null) {
                throw new RuntimeException("工号已存在");
            }
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setRole(request.getRole());
        user.setStudentNo(request.getStudentNo());
        user.setTeacherNo(request.getTeacherNo());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(1);

        userMapper.insert(user);
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public List<User> findAll(String role, String keyword) {
        return userMapper.findAll(role, keyword);
    }

    @Override
    public List<User> findTeachers() {
        return userMapper.findTeachers();
    }

    @Override
    public User update(User user) {
        findById(user.getId());
        userMapper.update(user);
        return findById(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        userMapper.deleteById(id);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = findById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(updateUser);
    }
}
