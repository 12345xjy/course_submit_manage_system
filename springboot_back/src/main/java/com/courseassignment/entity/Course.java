package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程实体类
 */
@Data
public class Course {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Long teacherId;
    private String semester;
    private Integer status;     // 0-结课, 1-进行中
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联字段
    private String teacherName;
    private Integer studentCount;
}
