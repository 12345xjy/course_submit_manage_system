package com.courseassignment.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 作业提交统计 DTO
 * 对单个作业的提交与批改进度进行多维度分析
 */
@Data
public class AssignmentStatsDTO {
    // 作业基本信息
    private Long assignmentId;
    private String assignmentTitle;
    private String courseName;
    private String teacherName;
    private LocalDateTime deadline;
    private Double maxScore;

    // 提交分析（作业-level）
    private Integer totalStudents;      // 应提交人数（选课人数）
    private Integer submittedCount;     // 已提交人数
    private Integer unsubmittedCount;   // 未提交人数
    private Double submissionRate;      // 提交率

    // 迟交分析
    private Integer onTimeCount;        // 按时提交数
    private Integer lateCount;          // 迟交数
    private Double lateRate;            // 迟交率

    // 批改分析
    private Integer gradedCount;        // 已批改数
    private Integer ungradedCount;      // 待批改数
    private Double gradingProgress;     // 批改进度

    // 成绩分析（仅已批改）
    private Double averageScore;        // 平均分
    private Double highestScore;        // 最高分
    private Double lowestScore;         // 最低分
    private Double passRate;            // 及格率
    private Integer excellentCount;     // 优秀人数 (≥90)
    private Integer goodCount;          // 良好人数 (80-89)
    private Integer mediumCount;        // 中等人数 (70-79)
    private Integer passCount;          // 及格人数 (60-69)
    private Integer failCount;          // 不及格人数 (<60)
}
