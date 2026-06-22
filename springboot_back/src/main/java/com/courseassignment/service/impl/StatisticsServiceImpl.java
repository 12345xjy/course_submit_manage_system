package com.courseassignment.service.impl;

import com.courseassignment.dto.*;
import com.courseassignment.mapper.StatisticsMapper;
import com.courseassignment.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 综合统计服务实现
 * 所有查询均委托给 StatisticsMapper（XML 手写 SQL）
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;

    public StatisticsServiceImpl(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    @Override
    public List<CourseStatsDTO> getCourseStats(Long teacherId, String keyword) {
        return statisticsMapper.getCourseComprehensiveStats(teacherId, keyword);
    }

    @Override
    public CourseStatsDTO getSingleCourseStats(Long courseId) {
        return statisticsMapper.getSingleCourseStats(courseId);
    }

    @Override
    public List<StudentGradeSummaryDTO> getStudentSummary(String keyword) {
        return statisticsMapper.getStudentGradeSummary(keyword);
    }

    @Override
    public StudentGradeSummaryDTO getSingleStudentSummary(Long studentId) {
        return statisticsMapper.getSingleStudentSummary(studentId);
    }

    @Override
    public List<StudentGradeSummaryDTO> getStudentRankingByCourse(Long courseId) {
        return statisticsMapper.getStudentRankingByCourse(courseId);
    }

    @Override
    public List<AssignmentStatsDTO> getAssignmentStatsByCourse(Long courseId) {
        return statisticsMapper.getAssignmentStatsByCourse(courseId);
    }

    @Override
    public AssignmentStatsDTO getSingleAssignmentStats(Long assignmentId) {
        return statisticsMapper.getSingleAssignmentStats(assignmentId);
    }

    @Override
    public List<TeacherWorkloadDTO> getTeacherWorkload() {
        return statisticsMapper.getTeacherWorkloadStats();
    }

    @Override
    public TeacherWorkloadDTO getSingleTeacherWorkload(Long teacherId) {
        return statisticsMapper.getSingleTeacherWorkload(teacherId);
    }

    @Override
    public Map<String, Object> getSystemOverviewStats() {
        return statisticsMapper.getSystemOverviewStats();
    }

    @Override
    public Map<String, Object> getStudentDashboardStats(Long studentId) {
        return statisticsMapper.getStudentDashboardStats(studentId);
    }

    @Override
    public Map<String, Object> getTeacherDashboardStats(Long teacherId) {
        return statisticsMapper.getTeacherDashboardStats(teacherId);
    }
}
