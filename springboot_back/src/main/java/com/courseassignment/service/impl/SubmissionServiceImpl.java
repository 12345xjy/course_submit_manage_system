package com.courseassignment.service.impl;

import com.courseassignment.entity.Assignment;
import com.courseassignment.entity.Submission;
import com.courseassignment.mapper.AssignmentMapper;
import com.courseassignment.mapper.SubmissionMapper;
import com.courseassignment.service.FileService;
import com.courseassignment.service.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 作业提交服务实现类
 */
@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionMapper submissionMapper;
    private final AssignmentMapper assignmentMapper;
    private final FileService fileService;

    public SubmissionServiceImpl(SubmissionMapper submissionMapper, AssignmentMapper assignmentMapper,
                                  FileService fileService) {
        this.submissionMapper = submissionMapper;
        this.assignmentMapper = assignmentMapper;
        this.fileService = fileService;
    }

    @Override
    public Submission submit(Long assignmentId, Long studentId, MultipartFile file) {
        // 检查是否已提交
        Long count = submissionMapper.countByAssignmentAndStudent(assignmentId, studentId);
        if (count > 0) {
            throw new RuntimeException("您已提交过该作业，请勿重复提交");
        }

        // 检查作业是否存在且有效
        Assignment assignment = assignmentMapper.findById(assignmentId);
        if (assignment == null || assignment.getStatus() == 0) {
            throw new RuntimeException("作业不存在或已关闭");
        }

        // 上传文件
        String filePath = fileService.upload(file);

        // 判断是否迟交
        int isLate = LocalDateTime.now().isAfter(assignment.getDeadline()) ? 1 : 0;

        Submission submission = new Submission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setFileName(file.getOriginalFilename());
        submission.setFilePath(filePath);
        submission.setFileSize(file.getSize());
        submission.setIsLate(isLate);
        submission.setStatus("SUBMITTED");

        submissionMapper.insert(submission);
        return findById(submission.getId());
    }

    @Override
    public Submission findById(Long id) {
        Submission submission = submissionMapper.findById(id);
        if (submission == null) {
            throw new RuntimeException("提交记录不存在");
        }
        return submission;
    }

    @Override
    public List<Submission> findByAssignmentId(Long assignmentId) {
        return submissionMapper.findByAssignmentId(assignmentId);
    }

    @Override
    public List<Submission> findByStudentId(Long studentId) {
        return submissionMapper.findByStudentId(studentId);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        submissionMapper.deleteById(id);
    }
}
