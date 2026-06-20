package com.courseassignment.service.impl;

import com.courseassignment.entity.Course;
import com.courseassignment.entity.CourseStudent;
import com.courseassignment.entity.User;
import com.courseassignment.mapper.CourseMapper;
import com.courseassignment.mapper.CourseStudentMapper;
import com.courseassignment.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务实现类
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseStudentMapper courseStudentMapper;

    public CourseServiceImpl(CourseMapper courseMapper, CourseStudentMapper courseStudentMapper) {
        this.courseMapper = courseMapper;
        this.courseStudentMapper = courseStudentMapper;
    }

    @Override
    public Course create(Course course) {
        courseMapper.insert(course);
        return findById(course.getId());
    }

    @Override
    public Course findById(Long id) {
        Course course = courseMapper.findById(id);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        return course;
    }

    @Override
    public List<Course> findAll(Long teacherId, String keyword) {
        return courseMapper.findAll(teacherId, keyword);
    }

    @Override
    public List<Course> findByStudentId(Long studentId) {
        return courseMapper.findByStudentId(studentId);
    }

    @Override
    public Course update(Course course) {
        findById(course.getId());
        courseMapper.update(course);
        return findById(course.getId());
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        courseMapper.deleteById(id);
    }

    @Override
    public void enrollStudent(Long courseId, Long studentId) {
        findById(courseId);
        CourseStudent existing = courseStudentMapper.findByCourseAndStudent(courseId, studentId);
        if (existing != null) {
            throw new RuntimeException("该学生已选修此课程");
        }
        CourseStudent cs = new CourseStudent();
        cs.setCourseId(courseId);
        cs.setStudentId(studentId);
        courseStudentMapper.insert(cs);
    }

    @Override
    public void unenrollStudent(Long courseId, Long studentId) {
        courseStudentMapper.delete(courseId, studentId);
    }

    @Override
    public List<User> getCourseStudents(Long courseId) {
        findById(courseId);
        return courseStudentMapper.findStudentsByCourseId(courseId);
    }
}
