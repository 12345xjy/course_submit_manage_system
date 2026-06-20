package com.courseassignment.service;

import com.courseassignment.dto.GradeRequest;
import com.courseassignment.entity.Grade;

import java.util.List;

/**
 * 成绩/批改服务接口
 */
public interface GradeService {

    /**
     * 批改/评分
     */
    Grade grade(GradeRequest request, Long teacherId);

    /**
     * 查询成绩详情
     */
    Grade findById(Long id);

    /**
     * 根据提交ID查询成绩
     */
    Grade findBySubmissionId(Long submissionId);

    /**
     * 查询某作业的所有成绩
     */
    List<Grade> findByAssignmentId(Long assignmentId);

    /**
     * 查询某学生的所有成绩
     */
    List<Grade> findByStudentId(Long studentId);

    /**
     * 更新成绩
     */
    Grade update(Long id, GradeRequest request);

    /**
     * 删除成绩
     */
    void deleteById(Long id);
}
