package com.courseassignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 作业创建/更新请求DTO
 */
@Data
public class AssignmentRequest {

    private Long courseId; // 创建时必填，更新时选填

    @NotBlank(message = "作业标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "截止时间不能为空")
    private LocalDateTime deadline;

    private Double maxScore = 100.0;
    private String fileTypes;
    private Integer maxSize = 10;
}
