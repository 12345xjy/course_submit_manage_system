package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.dto.AssignmentRequest;
import com.courseassignment.entity.Assignment;
import com.courseassignment.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作业管理控制器
 */
@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /**
     * 查询所有作业
     */
    @GetMapping("/assignments")
    public Result<List<Assignment>> getAllAssignments(@RequestParam(required = false) Long courseId,
                                                       @RequestParam(required = false) Integer status,
                                                       @RequestAttribute(value = "userId", required = false) Long userId,
                                                       @RequestAttribute(value = "role", required = false) String role) {
        // 教师只能看到自己课程的作业
        Long teacherId = "TEACHER".equals(role) ? userId : null;
        List<Assignment> assignments = assignmentService.findAll(courseId, teacherId, status);
        return Result.success(assignments);
    }

    /**
     * 查询作业详情（学生只看自己的提交数据）
     */
    @GetMapping("/assignments/{id}")
    public Result<Assignment> getAssignment(@PathVariable Long id,
                                             @RequestAttribute(value = "userId", required = false) Long userId,
                                             @RequestAttribute(value = "role", required = false) String role) {
        Assignment assignment;
        if ("STUDENT".equals(role) && userId != null) {
            assignment = assignmentService.findByIdForStudent(id, userId);
        } else {
            assignment = assignmentService.findById(id);
        }
        return Result.success(assignment);
    }

    /**
     * 教师 - 创建作业
     */
    @PostMapping("/teacher/assignments")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Assignment> createAssignment(@Valid @RequestBody AssignmentRequest request) {
        Assignment assignment = assignmentService.create(request);
        return Result.success("作业发布成功", assignment);
    }

    /**
     * 教师 - 更新作业
     */
    @PutMapping("/teacher/assignments/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Assignment> updateAssignment(@PathVariable Long id,
                                                @Valid @RequestBody AssignmentRequest request) {
        Assignment assignment = assignmentService.update(id, request);
        return Result.success("作业更新成功", assignment);
    }

    /**
     * 教师 - 删除作业
     */
    @DeleteMapping("/teacher/assignments/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteById(id);
        return Result.success();
    }

    /**
     * 学生 - 查询我的作业
     */
    @GetMapping("/student/assignments")
    public Result<List<Assignment>> getMyAssignments(@RequestAttribute("userId") Long userId) {
        List<Assignment> assignments = assignmentService.findByStudentId(userId);
        return Result.success(assignments);
    }
}
