package com.courseassignment.dto;

import lombok.Data;

/**
 * 教师工作负载统计 DTO
 * 统计教师的授课、发布作业、批改工作量
 */
@Data
public class TeacherWorkloadDTO {
    // 教师基本信息
    private Long teacherId;
    private String teacherName;
    private String teacherNo;

    // 课程负载
    private Integer courseCount;            // 授课数量
    private Integer totalStudents;          // 授课学生总数（去重）

    // 作业负载
    private Integer assignmentCount;        // 发布作业总数
    private Integer totalSubmissions;       // 收到的总提交数
    private Integer totalLateSubmissions;   // 迟交总数
    private Double lateRate;                // 迟交率

    // 批改负载
    private Integer gradedCount;            // 已批改数
    private Integer pendingGradingCount;    // 待批改数
    private Double gradingProgress;         // 批改进度 (%)
    private Double averageScoreGiven;       // 给出的平均分
}
