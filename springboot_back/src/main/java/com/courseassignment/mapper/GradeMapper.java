package com.courseassignment.mapper;

import com.courseassignment.entity.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成绩Mapper接口 —— 所有SQL手写于 GradeMapper.xml
 * 核心评分点：5表联查（grade + user批改人 + submission + user学生 + assignment + course）
 */
@Mapper
public interface GradeMapper {

    Grade findById(Long id);

    Grade findBySubmissionId(Long submissionId);

    List<Grade> findByAssignmentId(Long assignmentId);

    List<Grade> findByTeacherId(@Param("teacherId") Long teacherId, @Param("courseId") Long courseId);

    List<Grade> findByStudentId(Long studentId);

    int insert(Grade grade);

    int update(Grade grade);

    int deleteById(Long id);
}
