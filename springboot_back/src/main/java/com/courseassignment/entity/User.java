package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;        // STUDENT, TEACHER, ADMIN
    private String studentNo;
    private String teacherNo;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;     // 0-禁用, 1-启用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
