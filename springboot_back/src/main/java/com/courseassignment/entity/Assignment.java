package com.courseassignment.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 作业实体类
 */
@Data
public class Assignment {
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Double maxScore;
    private String fileTypes;
    private Integer maxSize;
    private Integer status;     // 0-已关闭, 1-进行中
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联字段
    private String courseName;
    private Integer submissionCount;
    private Integer gradedCount;
}
