package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.dto.GradeRequest;
import com.courseassignment.entity.Grade;
import com.courseassignment.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成绩/批改控制器
 */
@RestController
@RequestMapping("/api")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    /**
     * 教师 - 批改评分
     */
    @PostMapping("/teacher/grades")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<Grade> gradeSubmission(@Valid @RequestBody GradeRequest request,
                                          @RequestAttribute("userId") Long userId) {
        try {
            Grade grade = gradeService.grade(request, userId);
            return Result.success("批改成功", grade);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询成绩详情
     */
    @GetMapping("/grades/{id}")
    public Result<Grade> getGrade(@PathVariable Long id) {
        Grade grade = gradeService.findById(id);
        return Result.success(grade);
    }

    /**
     * 根据提交ID查询成绩
     */
    @GetMapping("/grades/submission/{submissionId}")
    public Result<Grade> getGradeBySubmission(@PathVariable Long submissionId) {
        Grade grade = gradeService.findBySubmissionId(submissionId);
        return Result.success(grade);
    }

    /**
     * 教师 - 查询某作业所有成绩
     */
    @GetMapping("/teacher/grades")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<List<Grade>> getGradesByAssignment(@RequestParam Long assignmentId) {
        List<Grade> grades = gradeService.findByAssignmentId(assignmentId);
        return Result.success(grades);
    }

    /**
     * 学生 - 查询我的成绩
     */
    @GetMapping("/student/grades")
    public Result<List<Grade>> getMyGrades(@RequestAttribute("userId") Long userId) {
        List<Grade> grades = gradeService.findByStudentId(userId);
        return Result.success(grades);
    }

    /**
     * 教师 - 更新成绩
     */
    @PutMapping("/teacher/grades/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<Grade> updateGrade(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        Grade grade = gradeService.update(id, request);
        return Result.success("成绩更新成功", grade);
    }

    /**
     * 教师 - 删除成绩
     */
    @DeleteMapping("/teacher/grades/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteById(id);
        return Result.success("成绩删除成功");
    }
}
