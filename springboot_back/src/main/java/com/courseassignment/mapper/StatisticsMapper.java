package com.courseassignment.mapper;

import com.courseassignment.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 综合统计 Mapper 接口
 * 所有复杂查询、聚合统计、多表联查均通过此接口对应的 XML 实现
 * —— 核心评分点：手写 MyBatis XML 多表联动业务查询
 */
@Mapper
public interface StatisticsMapper {

    // ==================== 课程维度统计 ====================

    /**
     * 查询所有课程的综合统计（提交率、均分、及格率、迟交率等）
     * SQL: 6表联查 + 嵌套子查询 + 多聚合函数
     */
    List<CourseStatsDTO> getCourseComprehensiveStats(@Param("teacherId") Long teacherId,
                                                      @Param("keyword") String keyword);

    /**
     * 查询单个课程的详细统计
     */
    CourseStatsDTO getSingleCourseStats(@Param("courseId") Long courseId);

    // ==================== 学生维度统计 ====================

    /**
     * 查询所有学生的成绩汇总（跨课程加权平均、提交率、排名）
     * SQL: 5表联查 + 相关子查询 + 条件聚合
     */
    List<StudentGradeSummaryDTO> getStudentGradeSummary(@Param("keyword") String keyword);

    /**
     * 查询单个学生的详细成绩汇总
     */
    StudentGradeSummaryDTO getSingleStudentSummary(@Param("studentId") Long studentId);

    /**
     * 查询某课程学生的成绩排名（含排名序号）
     * SQL: 窗口函数 / 变量模拟排名
     */
    List<StudentGradeSummaryDTO> getStudentRankingByCourse(@Param("courseId") Long courseId);

    // ==================== 作业维度统计 ====================

    /**
     * 查询某课程下所有作业的提交与批改统计
     * SQL: 4表联查 + 多重条件聚合 + 分数段分布
     */
    List<AssignmentStatsDTO> getAssignmentStatsByCourse(@Param("courseId") Long courseId);

    /**
     * 查询单个作业的详细提交与批改统计（含分数段分布）
     */
    AssignmentStatsDTO getSingleAssignmentStats(@Param("assignmentId") Long assignmentId);

    // ==================== 教师维度统计 ====================

    /**
     * 查询所有教师的工作负载统计
     * SQL: 5表联查 + 去重计数 + 进度计算
     */
    List<TeacherWorkloadDTO> getTeacherWorkloadStats();

    /**
     * 查询单个教师的工作负载详情
     */
    TeacherWorkloadDTO getSingleTeacherWorkload(@Param("teacherId") Long teacherId);

    // ==================== 全局仪表盘统计 ====================

    /**
     * 系统总览统计（管理员仪表盘）
     * 返回：用户总数、课程总数、作业总数、提交总数、批改总数、系统提交率、系统及格率
     */
    java.util.Map<String, Object> getSystemOverviewStats();

    /**
     * 学生个人仪表盘统计
     * 返回：选课数、待提交数、已提交数、已批改数、平均分
     */
    java.util.Map<String, Object> getStudentDashboardStats(@Param("studentId") Long studentId);

    /**
     * 教师个人仪表盘统计
     * 返回：授课数、发布作业数、待批改数、学生总数
     */
    java.util.Map<String, Object> getTeacherDashboardStats(@Param("teacherId") Long teacherId);
}
