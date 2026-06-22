package com.courseassignment.dto;

import lombok.Data;

/**
 * 学生成绩汇总 DTO
 * 跨课程、跨作业聚合统计学生的综合表现
 */
@Data
public class StudentGradeSummaryDTO {
    // 学生基本信息
    private Long studentId;
    private String studentName;
    private String studentNo;

    // 学业统计（跨所有课程）
    private Integer enrolledCourseCount;    // 选课总数
    private Integer totalAssignments;       // 全部作业数
    private Integer submittedCount;         // 已提交数
    private Integer gradedCount;            // 已批改数
    private Double submissionRate;          // 提交率

    // 成绩统计
    private Double overallAverage;          // 加权平均分
    private Double highestScore;            // 单次最高分
    private Double lowestScore;             // 单次最低分
    private Integer passCount;              // 及格次数
    private Integer failCount;              // 不及格次数
    private Double passRate;                // 及格率

    // 课程明细（JSON 格式，前端解析）
    private String courseDetails;           // 每个课程的表现明细
}
