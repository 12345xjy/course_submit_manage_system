package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.dto.LoginRequest;
import com.courseassignment.dto.LoginResponse;
import com.courseassignment.dto.RegisterRequest;
import com.courseassignment.entity.User;
import com.courseassignment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return Result.success("登录成功", response);
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }

    /**
     * [临时] 生成密码哈希 - 获取正确哈希后请删除此端点
     */
    @GetMapping("/gen-hash")
    public Result<Map<String, String>> genHash(@RequestParam(defaultValue = "123456") String pwd) {
        String hash = passwordEncoder.encode(pwd);
        return Result.success(Map.of("password", pwd, "bcryptHash", hash));
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return Result.success("注册成功", user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
