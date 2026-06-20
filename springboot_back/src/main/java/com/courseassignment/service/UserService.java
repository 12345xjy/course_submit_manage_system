package com.courseassignment.service;

import com.courseassignment.dto.LoginRequest;
import com.courseassignment.dto.LoginResponse;
import com.courseassignment.dto.RegisterRequest;
import com.courseassignment.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户注册
     */
    User register(RegisterRequest request);

    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 查询所有用户（支持筛选）
     */
    List<User> findAll(String role, String keyword);

    /**
     * 查询所有教师
     */
    List<User> findTeachers();

    /**
     * 更新用户信息
     */
    User update(User user);

    /**
     * 删除用户
     */
    void deleteById(Long id);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}
