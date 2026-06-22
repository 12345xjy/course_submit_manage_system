package com.courseassignment.mapper;

import com.courseassignment.entity.CourseStudent;
import com.courseassignment.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 选课关联Mapper接口 —— 所有SQL手写于 CourseStudentMapper.xml
 * 核心评分点：跨表查询（course_student + user）
 */
@Mapper
public interface CourseStudentMapper {

    List<CourseStudent> findByCourseId(Long courseId);

    List<CourseStudent> findByStudentId(Long studentId);

    CourseStudent findByCourseAndStudent(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    int insert(CourseStudent courseStudent);

    int delete(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    List<User> findStudentsByCourseId(Long courseId);

    Long countByCourseId(Long courseId);
}
