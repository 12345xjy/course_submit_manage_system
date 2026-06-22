package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.dto.*;
import com.courseassignment.service.StatisticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 综合统计控制器
 * 全部接口的数据来源于 MyBatis XML 手写的多表联动查询
 * —— 核心评分点展示
 */
@RestController
@RequestMapping("/api")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    // ==================== 课程维度统计 ====================

    /**
     * 课程综合统计列表（所有课程的多维度数据）
     * 教师只能看自己课程的统计
     */
    @GetMapping("/stats/courses")
    public Result<List<CourseStatsDTO>> getCourseStats(
            @RequestAttribute(value = "userId", required = false) Long userId,
            @RequestAttribute(value = "role", required = false) String role,
            @RequestParam(required = false) String keyword) {
        Long teacherId = "TEACHER".equals(role) ? userId : null;
        return Result.success(statisticsService.getCourseStats(teacherId, keyword));
    }

    /**
     * 单个课程详细统计
     */
    @GetMapping("/stats/courses/{courseId}")
    public Result<CourseStatsDTO> getSingleCourseStats(@PathVariable Long courseId) {
        return Result.success(statisticsService.getSingleCourseStats(courseId));
    }

    // ==================== 学生维度统计 ====================

    /**
     * 学生成绩汇总（跨课程加权平均、提交率、排名）
     * 教师和管理员可查看
     */
    @GetMapping("/stats/students")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<List<StudentGradeSummaryDTO>> getStudentSummary(
            @RequestParam(required = false) String keyword) {
        return Result.success(statisticsService.getStudentSummary(keyword));
    }

    /**
     * 单个学生成绩汇总
     */
    @GetMapping("/stats/students/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<StudentGradeSummaryDTO> getSingleStudentSummary(@PathVariable Long studentId) {
        return Result.success(statisticsService.getSingleStudentSummary(studentId));
    }

    /**
     * 某课程学生成绩排名
     */
    @GetMapping("/stats/courses/{courseId}/ranking")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<List<StudentGradeSummaryDTO>> getStudentRanking(@PathVariable Long courseId) {
        return Result.success(statisticsService.getStudentRankingByCourse(courseId));
    }

    // ==================== 作业维度统计 ====================

    /**
     * 某课程下所有作业的提交与批改统计（含分数段分布）
     */
    @GetMapping("/stats/courses/{courseId}/assignments")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<List<AssignmentStatsDTO>> getAssignmentStats(@PathVariable Long courseId) {
        return Result.success(statisticsService.getAssignmentStatsByCourse(courseId));
    }

    /**
     * 单个作业详细统计（提交率、迟交率、分数段分布、批改进度）
     */
    @GetMapping("/stats/assignments/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<AssignmentStatsDTO> getSingleAssignmentStats(@PathVariable Long assignmentId) {
        return Result.success(statisticsService.getSingleAssignmentStats(assignmentId));
    }

    // ==================== 教师维度统计 ====================

    /**
     * 教师工作负载统计（管理员视图）
     */
    @GetMapping("/stats/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<TeacherWorkloadDTO>> getTeacherWorkload() {
        return Result.success(statisticsService.getTeacherWorkload());
    }

    /**
     * 单个教师工作负载详情
     */
    @GetMapping("/stats/teachers/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<TeacherWorkloadDTO> getSingleTeacherWorkload(@PathVariable Long teacherId) {
        return Result.success(statisticsService.getSingleTeacherWorkload(teacherId));
    }

    // ==================== 仪表盘统计 ====================

    /**
     * 管理员仪表盘总览统计
     */
    @GetMapping("/stats/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getSystemOverview() {
        return Result.success(statisticsService.getSystemOverviewStats());
    }

    /**
     * 学生个人仪表盘统计
     */
    @GetMapping("/student/stats/dashboard")
    public Result<Map<String, Object>> getStudentDashboard(
            @RequestAttribute("userId") Long userId) {
        return Result.success(statisticsService.getStudentDashboardStats(userId));
    }

    /**
     * 教师个人仪表盘统计
     */
    @GetMapping("/teacher/stats/dashboard")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Map<String, Object>> getTeacherDashboard(
            @RequestAttribute("userId") Long userId) {
        return Result.success(statisticsService.getTeacherDashboardStats(userId));
    }
}
