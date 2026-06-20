package com.courseassignment.mapper;

import com.courseassignment.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {

    @Select("SELECT c.*, u.real_name AS teacher_name, " +
            "(SELECT COUNT(*) FROM course_student cs WHERE cs.course_id = c.id) AS student_count " +
            "FROM course c LEFT JOIN user u ON c.teacher_id = u.id WHERE c.id = #{id}")
    Course findById(Long id);

    @Select("<script>" +
            "SELECT c.*, u.real_name AS teacher_name, " +
            "(SELECT COUNT(*) FROM course_student cs WHERE cs.course_id = c.id) AS student_count " +
            "FROM course c LEFT JOIN user u ON c.teacher_id = u.id WHERE 1=1" +
            "<if test='teacherId != null'> AND c.teacher_id = #{teacherId}</if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (c.name LIKE CONCAT('%',#{keyword},'%') OR c.code LIKE CONCAT('%',#{keyword},'%'))</if>" +
            "ORDER BY c.created_at DESC" +
            "</script>")
    List<Course> findAll(@Param("teacherId") Long teacherId, @Param("keyword") String keyword);

    @Select("SELECT c.*, u.real_name AS teacher_name, " +
            "(SELECT COUNT(*) FROM course_student cs WHERE cs.course_id = c.id) AS student_count " +
            "FROM course c LEFT JOIN user u ON c.teacher_id = u.id " +
            "WHERE c.id IN (SELECT course_id FROM course_student WHERE student_id = #{studentId}) " +
            "ORDER BY c.created_at DESC")
    List<Course> findByStudentId(Long studentId);

    @Insert("INSERT INTO course (name, code, description, teacher_id, semester) " +
            "VALUES (#{name}, #{code}, #{description}, #{teacherId}, #{semester})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Update("<script>" +
            "UPDATE course SET" +
            "<if test='name != null'> name = #{name},</if>" +
            "<if test='description != null'> description = #{description},</if>" +
            "<if test='semester != null'> semester = #{semester},</if>" +
            "<if test='status != null'> status = #{status},</if>" +
            "updated_at = NOW() WHERE id = #{id}" +
            "</script>")
    int update(Course course);

    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM course")
    Long count();
}
