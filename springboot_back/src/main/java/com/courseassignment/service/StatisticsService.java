package com.courseassignment.service;

import com.courseassignment.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 综合统计服务接口
 * 全部基于 MyBatis XML 手写的多表联动查询
 */
public interface StatisticsService {

    // ==================== 课程维度 ====================
    List<CourseStatsDTO> getCourseStats(Long teacherId, String keyword);
    CourseStatsDTO getSingleCourseStats(Long courseId);

    // ==================== 学生维度 ====================
    List<StudentGradeSummaryDTO> getStudentSummary(String keyword);
    StudentGradeSummaryDTO getSingleStudentSummary(Long studentId);
    List<StudentGradeSummaryDTO> getStudentRankingByCourse(Long courseId);

    // ==================== 作业维度 ====================
    List<AssignmentStatsDTO> getAssignmentStatsByCourse(Long courseId);
    AssignmentStatsDTO getSingleAssignmentStats(Long assignmentId);

    // ==================== 教师维度 ====================
    List<TeacherWorkloadDTO> getTeacherWorkload();
    TeacherWorkloadDTO getSingleTeacherWorkload(Long teacherId);

    // ==================== 仪表盘 ====================
    Map<String, Object> getSystemOverviewStats();
    Map<String, Object> getStudentDashboardStats(Long studentId);
    Map<String, Object> getTeacherDashboardStats(Long teacherId);
}
