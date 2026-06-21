package com.courseassignment.service.impl;

import com.courseassignment.dto.GradeRequest;
import com.courseassignment.entity.Grade;
import com.courseassignment.entity.Notification;
import com.courseassignment.entity.Submission;
import com.courseassignment.mapper.GradeMapper;
import com.courseassignment.mapper.NotificationMapper;
import com.courseassignment.mapper.SubmissionMapper;
import com.courseassignment.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成绩/批改服务实现类
 */
@Service
@Transactional
public class GradeServiceImpl implements GradeService {

    private final GradeMapper gradeMapper;
    private final SubmissionMapper submissionMapper;
    private final NotificationMapper notificationMapper;

    public GradeServiceImpl(GradeMapper gradeMapper, SubmissionMapper submissionMapper,
                             NotificationMapper notificationMapper) {
        this.gradeMapper = gradeMapper;
        this.submissionMapper = submissionMapper;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Grade grade(GradeRequest request, Long teacherId) {
        // 检查提交是否存在
        Submission submission = submissionMapper.findById(request.getSubmissionId());
        if (submission == null) {
            throw new RuntimeException("提交记录不存在");
        }

        // 检查是否已批改
        Grade existing = gradeMapper.findBySubmissionId(request.getSubmissionId());
        if (existing != null) {
            throw new RuntimeException("该提交已批改，如需修改请使用更新功能");
        }

        Grade grade = new Grade();
        grade.setSubmissionId(request.getSubmissionId());
        grade.setScore(request.getScore());
        grade.setComment(request.getComment());
        grade.setGradedBy(teacherId);

        gradeMapper.insert(grade);

        // 更新提交状态为已批改
        submissionMapper.updateStatus(submission.getId(), "GRADED");

        // 发送通知给该学生
        Notification notification = new Notification();
        notification.setUserId(submission.getStudentId());
        notification.setTitle("作业已批改");
        notification.setContent("您的作业「" + submission.getAssignmentTitle() + "」已批改，得分：" + request.getScore() + "分。");
        notification.setType("GRADE");
        notificationMapper.insert(notification);

        return findById(grade.getId());
    }

    @Override
    public Grade findById(Long id) {
        Grade grade = gradeMapper.findById(id);
        if (grade == null) {
            throw new RuntimeException("成绩记录不存在");
        }
        return grade;
    }

    @Override
    public Grade findBySubmissionId(Long submissionId) {
        return gradeMapper.findBySubmissionId(submissionId);
    }

    @Override
    public List<Grade> findByAssignmentId(Long assignmentId) {
        return gradeMapper.findByAssignmentId(assignmentId);
    }

    @Override
    public List<Grade> findByTeacherId(Long teacherId, Long courseId) {
        return gradeMapper.findByTeacherId(teacherId, courseId);
    }

    @Override
    public List<Grade> findByStudentId(Long studentId) {
        return gradeMapper.findByStudentId(studentId);
    }

    @Override
    public Grade update(Long id, GradeRequest request) {
        findById(id);

        Grade grade = new Grade();
        grade.setId(id);
        grade.setScore(request.getScore());
        grade.setComment(request.getComment());

        gradeMapper.update(grade);
        return findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Grade grade = findById(id);
        // 恢复提交状态
        submissionMapper.updateStatus(grade.getSubmissionId(), "SUBMITTED");
        gradeMapper.deleteById(id);
    }
}
