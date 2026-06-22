package com.courseassignment.mapper;

import com.courseassignment.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程Mapper接口 —— 所有SQL手写于 CourseMapper.xml
 * 核心评分点：多表联查（course + user + course_student）
 */
@Mapper
public interface CourseMapper {

    Course findById(Long id);

    List<Course> findAll(@Param("teacherId") Long teacherId, @Param("keyword") String keyword);

    List<Course> findByStudentId(Long studentId);

    int insert(Course course);

    int update(Course course);

    int deleteById(Long id);

    Long count();
}
