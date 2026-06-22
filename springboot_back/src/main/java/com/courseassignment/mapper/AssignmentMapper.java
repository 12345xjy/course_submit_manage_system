package com.courseassignment.mapper;

import com.courseassignment.entity.Assignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业Mapper接口 —— 所有SQL手写于 AssignmentMapper.xml
 * 核心评分点：多表联查（assignment + course + submission + grade）
 */
@Mapper
public interface AssignmentMapper {

    Assignment findById(Long id);

    Assignment findByIdWithStudent(@Param("id") Long id, @Param("studentId") Long studentId);

    List<Assignment> findAll(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId, @Param("status") Integer status);

    List<Assignment> findByStudentId(Long studentId);

    int insert(Assignment assignment);

    int update(Assignment assignment);

    int deleteById(Long id);

    Long countByCourseId(Long courseId);
}
