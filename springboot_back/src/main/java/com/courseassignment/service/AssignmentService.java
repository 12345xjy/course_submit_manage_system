package com.courseassignment.service;

import com.courseassignment.dto.AssignmentRequest;
import com.courseassignment.entity.Assignment;

import java.util.List;

/**
 * 作业服务接口
 */
public interface AssignmentService {

    /**
     * 创建作业
     */
    Assignment create(AssignmentRequest request);

    /**
     * 查询作业详情
     */
    Assignment findById(Long id);

    /**
     * 查询所有作业（支持按课程筛选）
     */
    List<Assignment> findAll(Long courseId, Integer status);

    /**
     * 查询学生的作业列表
     */
    List<Assignment> findByStudentId(Long studentId);

    /**
     * 更新作业
     */
    Assignment update(Long id, AssignmentRequest request);

    /**
     * 删除作业
     */
    void deleteById(Long id);
}
