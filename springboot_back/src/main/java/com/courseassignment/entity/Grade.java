package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 成绩/批改实体类
 */
@Data
public class Grade {
    private Long id;
    private Long submissionId;
    private Double score;
    private String comment;
    private Long gradedBy;
    private LocalDateTime gradedTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联字段
    private String gradedByName;
    private String studentName;
    private String assignmentTitle;
}
