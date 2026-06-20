package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程-学生关联实体类
 */
@Data
public class CourseStudent {
    private Long id;
    private Long courseId;
    private Long studentId;
    private LocalDateTime createdAt;
}
