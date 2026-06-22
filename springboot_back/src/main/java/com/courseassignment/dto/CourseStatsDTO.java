package com.courseassignment.dto;

import lombok.Data;

/**
 * 课程综合统计 DTO
 * 用于 MyBatis XML 多表聚合查询结果映射
 */
@Data
public class CourseStatsDTO {
    // 课程基本信息（来自 course + user 联表）
    private Long courseId;
    private String courseName;
    private String courseCode;
    private String teacherName;
    private String semester;

    // 学生统计（来自 course_student 子查询）
    private Integer enrolledCount;      // 选课人数

    // 作业统计（来自 assignment 子查询）
    private Integer assignmentCount;    // 作业总数

    // 提交统计（来自 submission 联表）
    private Integer submittedCount;     // 已提交人次
    private Integer unsubmittedCount;   // 未提交人次
    private Double submissionRate;      // 提交率 (百分比)
    private Integer lateCount;          // 迟交人次
    private Double lateRate;            // 迟交率 (百分比)

    // 成绩统计（来自 grade 联表）
    private Double averageScore;        // 课程平均分
    private Double highestScore;        // 最高分
    private Double lowestScore;         // 最低分
    private Double passRate;            // 及格率 (≥60)
    private Integer gradedCount;        // 已批改人次
}
