package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 作业提交实体类
 */
@Data
public class Submission {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private LocalDateTime submitTime;
    private Integer isLate;     // 0-否, 1-是
    private String status;      // SUBMITTED-已提交, GRADED-已批改
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联字段
    private String studentName;
    private String studentNo;
    private String assignmentTitle;
    private String courseName;
    private Double score;
    private String comment;
}
