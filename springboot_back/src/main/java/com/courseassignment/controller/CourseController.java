package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.entity.Course;
import com.courseassignment.entity.User;
import com.courseassignment.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程管理控制器
 */
@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * 查询所有课程
     */
    @GetMapping("/courses")
    public Result<List<Course>> getAllCourses(@RequestParam(required = false) Long teacherId,
                                               @RequestParam(required = false) String keyword) {
        List<Course> courses = courseService.findAll(teacherId, keyword);
        return Result.success(courses);
    }

    /**
     * 查询课程详情
     */
    @GetMapping("/courses/{id}")
    public Result<Course> getCourse(@PathVariable Long id) {
        Course course = courseService.findById(id);
        return Result.success(course);
    }

    /**
     * 教师/管理员 - 创建课程
     */
    @PostMapping("/teacher/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Course> createCourse(@RequestBody Course course,
                                        @RequestAttribute("userId") Long userId,
                                        @RequestAttribute("role") String role) {
        // 非管理员只能为自己创建课程，管理员可为任意教师创建
        if (!"ADMIN".equals(role)) {
            course.setTeacherId(userId);
        }
        if (course.getTeacherId() == null) {
            course.setTeacherId(userId);
        }
        Course created = courseService.create(course);
        return Result.success("课程创建成功", created);
    }

    /**
     * 教师/管理员 - 更新课程
     */
    @PutMapping("/teacher/courses/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        Course updated = courseService.update(course);
        return Result.success("课程更新成功", updated);
    }

    /**
     * 教师/管理员 - 删除课程
     */
    @DeleteMapping("/teacher/courses/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询我的课程（学生视角）
     */
    @GetMapping("/student/courses")
    public Result<List<Course>> getMyCourses(@RequestAttribute("userId") Long userId) {
        List<Course> courses = courseService.findByStudentId(userId);
        return Result.success(courses);
    }

    /**
     * 查询我教授的课程（教师视角）
     */
    @GetMapping("/teacher/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<List<Course>> getMyTeachingCourses(@RequestAttribute("userId") Long userId) {
        List<Course> courses = courseService.findAll(userId, null);
        return Result.success(courses);
    }

    /**
     * 学生选课
     */
    @PostMapping("/student/courses/{courseId}/enroll")
    public Result<Void> enrollCourse(@PathVariable Long courseId,
                                      @RequestAttribute("userId") Long userId) {
        try {
            courseService.enrollStudent(courseId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 学生退课
     */
    @DeleteMapping("/student/courses/{courseId}/enroll")
    public Result<Void> unenrollCourse(@PathVariable Long courseId,
                                        @RequestAttribute("userId") Long userId) {
        courseService.unenrollStudent(courseId, userId);
        return Result.success();
    }

    /**
     * 获取课程的学生列表
     */
    @GetMapping("/teacher/courses/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<List<User>> getCourseStudents(@PathVariable Long courseId) {
        List<User> students = courseService.getCourseStudents(courseId);
        students.forEach(s -> s.setPassword(null));
        return Result.success(students);
    }
}
