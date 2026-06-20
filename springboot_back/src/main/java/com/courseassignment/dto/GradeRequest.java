package com.courseassignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 批改请求DTO
 */
@Data
public class GradeRequest {

    @NotNull(message = "提交ID不能为空")
    private Long submissionId;

    @NotNull(message = "分数不能为空")
    private Double score;

    private String comment;
}
