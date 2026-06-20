package com.courseassignment.service;

import com.courseassignment.entity.Course;
import com.courseassignment.entity.User;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {

    /**
     * 创建课程
     */
    Course create(Course course);

    /**
     * 查询课程详情
     */
    Course findById(Long id);

    /**
     * 查询所有课程（支持按教师筛选）
     */
    List<Course> findAll(Long teacherId, String keyword);

    /**
     * 查询某学生的选修课程
     */
    List<Course> findByStudentId(Long studentId);

    /**
     * 更新课程
     */
    Course update(Course course);

    /**
     * 删除课程
     */
    void deleteById(Long id);

    /**
     * 学生选课
     */
    void enrollStudent(Long courseId, Long studentId);

    /**
     * 学生退课
     */
    void unenrollStudent(Long courseId, Long studentId);

    /**
     * 获取课程学生列表
     */
    List<User> getCourseStudents(Long courseId);
}
