package com.courseassignment.mapper;

import com.courseassignment.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 提交Mapper接口 —— 所有SQL手写于 SubmissionMapper.xml
 * 核心评分点：5表联查（submission + user + assignment + course + grade）
 */
@Mapper
public interface SubmissionMapper {

    Submission findById(Long id);

    List<Submission> findByAssignmentId(Long assignmentId);

    List<Submission> findByStudentId(Long studentId);

    Submission findByAssignmentAndStudent(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);

    int insert(Submission submission);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int deleteById(Long id);

    Long countByAssignmentId(Long assignmentId);

    Long countByAssignmentAndStudent(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);
}
