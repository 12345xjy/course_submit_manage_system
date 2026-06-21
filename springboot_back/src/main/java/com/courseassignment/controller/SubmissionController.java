package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.entity.Submission;
import com.courseassignment.service.FileService;
import com.courseassignment.service.SubmissionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作业提交控制器
 */
@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final FileService fileService;

    public SubmissionController(SubmissionService submissionService, FileService fileService) {
        this.submissionService = submissionService;
        this.fileService = fileService;
    }

    /**
     * 学生 - 提交作业
     */
    @PostMapping("/student/submissions")
    public Result<Submission> submitAssignment(@RequestParam Long assignmentId,
                                                @RequestAttribute("userId") Long userId,
                                                @RequestParam("file") MultipartFile file) {
        try {
            Submission submission = submissionService.submit(assignmentId, userId, file);
            return Result.success("作业提交成功", submission);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询提交详情
     */
    @GetMapping("/submissions/{id}")
    public Result<Submission> getSubmission(@PathVariable Long id) {
        Submission submission = submissionService.findById(id);
        return Result.success(submission);
    }

    /**
     * 教师 - 查询某作业的所有提交
     */
    @GetMapping("/teacher/submissions")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public Result<List<Submission>> getSubmissionsByAssignment(@RequestParam Long assignmentId) {
        List<Submission> submissions = submissionService.findByAssignmentId(assignmentId);
        return Result.success(submissions);
    }

    /**
     * 学生 - 查询我的提交记录
     */
    @GetMapping("/student/submissions")
    public Result<List<Submission>> getMySubmissions(@RequestAttribute("userId") Long userId) {
        List<Submission> submissions = submissionService.findByStudentId(userId);
        return Result.success(submissions);
    }

    /**
     * 下载提交的文件
     */
    @GetMapping("/files/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        Resource resource = fileService.download(filePath);
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    /**
     * 删除提交记录（仅提交者本人可删除）
     */
    @DeleteMapping("/submissions/{id}")
    public Result<Void> deleteSubmission(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Submission submission = submissionService.findById(id);
        if (!submission.getStudentId().equals(userId)) {
            return Result.error("只能删除自己的提交记录");
        }
        submissionService.deleteById(id);
        return Result.success();
    }
}
