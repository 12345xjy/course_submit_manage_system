package com.courseassignment.service.impl;

import com.courseassignment.dto.AssignmentRequest;
import com.courseassignment.entity.Assignment;
import com.courseassignment.mapper.AssignmentMapper;
import com.courseassignment.mapper.CourseMapper;
import com.courseassignment.mapper.CourseStudentMapper;
import com.courseassignment.mapper.NotificationMapper;
import com.courseassignment.entity.Notification;
import com.courseassignment.entity.User;
import com.courseassignment.service.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作业服务实现类
 */
@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentMapper assignmentMapper;
    private final CourseMapper courseMapper;
    private final CourseStudentMapper courseStudentMapper;
    private final NotificationMapper notificationMapper;

    public AssignmentServiceImpl(AssignmentMapper assignmentMapper, CourseMapper courseMapper,
                                  CourseStudentMapper courseStudentMapper, NotificationMapper notificationMapper) {
        this.assignmentMapper = assignmentMapper;
        this.courseMapper = courseMapper;
        this.courseStudentMapper = courseStudentMapper;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Assignment create(AssignmentRequest request) {
        // 验证课程是否存在
        courseMapper.findById(request.getCourseId());

        Assignment assignment = new Assignment();
        assignment.setCourseId(request.getCourseId());
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDeadline(request.getDeadline());
        assignment.setMaxScore(request.getMaxScore() != null ? request.getMaxScore() : 100.0);
        assignment.setFileTypes(request.getFileTypes());
        assignment.setMaxSize(request.getMaxSize() != null ? request.getMaxSize() : 10);
        assignment.setStatus(1);

        assignmentMapper.insert(assignment);

        // 通知该课程的所有学生
        List<User> students = courseStudentMapper.findStudentsByCourseId(request.getCourseId());
        for (User student : students) {
            Notification notification = new Notification();
            notification.setUserId(student.getId());
            notification.setTitle("新作业发布");
            notification.setContent("课程有新作业：" + request.getTitle() + "，请及时完成并提交。");
            notification.setType("ASSIGNMENT");
            notificationMapper.insert(notification);
        }

        return findById(assignment.getId());
    }

    @Override
    public Assignment findById(Long id) {
        Assignment assignment = assignmentMapper.findById(id);
        if (assignment == null) {
            throw new RuntimeException("作业不存在");
        }
        return assignment;
    }

    @Override
    public List<Assignment> findAll(Long courseId, Integer status) {
        return assignmentMapper.findAll(courseId, status);
    }

    @Override
    public List<Assignment> findByStudentId(Long studentId) {
        return assignmentMapper.findByStudentId(studentId);
    }

    @Override
    public Assignment update(Long id, AssignmentRequest request) {
        findById(id);

        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDeadline(request.getDeadline());
        assignment.setMaxScore(request.getMaxScore());
        assignment.setFileTypes(request.getFileTypes());
        assignment.setMaxSize(request.getMaxSize());

        assignmentMapper.update(assignment);
        return findById(id);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        assignmentMapper.deleteById(id);
    }
}
