package com.courseassignment.mapper;

import com.courseassignment.entity.CourseStudent;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 课程-学生关联Mapper接口
 */
@Mapper
public interface CourseStudentMapper {

    @Select("SELECT * FROM course_student WHERE course_id = #{courseId}")
    List<CourseStudent> findByCourseId(Long courseId);

    @Select("SELECT * FROM course_student WHERE student_id = #{studentId}")
    List<CourseStudent> findByStudentId(Long studentId);

    @Select("SELECT * FROM course_student WHERE course_id = #{courseId} AND student_id = #{studentId}")
    CourseStudent findByCourseAndStudent(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Insert("INSERT INTO course_student (course_id, student_id) VALUES (#{courseId}, #{studentId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CourseStudent courseStudent);

    @Delete("DELETE FROM course_student WHERE course_id = #{courseId} AND student_id = #{studentId}")
    int delete(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Select("SELECT u.* FROM user u INNER JOIN course_student cs ON u.id = cs.student_id WHERE cs.course_id = #{courseId}")
    List<com.courseassignment.entity.User> findStudentsByCourseId(Long courseId);

    @Select("SELECT COUNT(*) FROM course_student WHERE course_id = #{courseId}")
    Long countByCourseId(Long courseId);
}
