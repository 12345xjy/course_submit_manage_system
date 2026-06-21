package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.entity.User;
import com.courseassignment.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/profile")
    public Result<User> getProfile(@RequestAttribute("userId") Long userId) {
        User user = userService.findById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/user/profile")
    public Result<User> updateProfile(@RequestAttribute("userId") Long userId, @RequestBody User user) {
        user.setId(userId);
        User updated = userService.update(user);
        updated.setPassword(null);
        return Result.success("个人信息更新成功", updated);
    }

    /**
     * 修改密码
     */
    @PutMapping("/user/password")
    public Result<Void> changePassword(@RequestAttribute("userId") Long userId,
                                        @RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (oldPassword == null || newPassword == null) {
            return Result.error("原密码和新密码不能为空");
        }
        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询所有教师列表（公开接口，注册时使用）
     */
    @GetMapping("/public/teachers")
    public Result<List<User>> getTeachers() {
        List<User> teachers = userService.findTeachers();
        teachers.forEach(t -> t.setPassword(null));
        return Result.success(teachers);
    }

    /**
     * 管理员 - 查询所有用户
     */
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<User>> getAllUsers(@RequestParam(required = false) String role,
                                           @RequestParam(required = false) String keyword) {
        List<User> users = userService.findAll(role, keyword);
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    /**
     * 管理员 - 禁用/启用用户
     */
    @PutMapping("/admin/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> toggleUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        if (status == null || (status != 0 && status != 1)) {
            return Result.error("状态值无效，必须为 0 或 1");
        }
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userService.update(user);
        return Result.success();
    }

    /**
     * 管理员 - 删除用户
     */
    @DeleteMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@PathVariable Long id, @RequestAttribute("userId") Long currentUserId) {
        if (id.equals(currentUserId)) {
            return Result.error("不能删除自己的账号");
        }
        userService.deleteById(id);
        return Result.success();
    }
}
