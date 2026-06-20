package com.courseassignment.service;

import com.courseassignment.entity.Submission;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作业提交服务接口
 */
public interface SubmissionService {

    /**
     * 提交作业
     */
    Submission submit(Long assignmentId, Long studentId, MultipartFile file);

    /**
     * 查询提交详情
     */
    Submission findById(Long id);

    /**
     * 查询某作业的所有提交
     */
    List<Submission> findByAssignmentId(Long assignmentId);

    /**
     * 查询某学生的所有提交
     */
    List<Submission> findByStudentId(Long studentId);

    /**
     * 删除提交
     */
    void deleteById(Long id);
}
