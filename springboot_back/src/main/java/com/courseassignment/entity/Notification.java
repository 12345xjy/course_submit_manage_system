package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统通知实体类
 */
@Data
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String type;        // SYSTEM, ASSIGNMENT, GRADE
    private Integer isRead;     // 0-未读, 1-已读
    private LocalDateTime createdAt;
}
