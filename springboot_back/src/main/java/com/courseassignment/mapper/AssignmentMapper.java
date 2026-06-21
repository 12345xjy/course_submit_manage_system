package com.courseassignment.mapper;

import com.courseassignment.entity.Assignment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作业Mapper接口
 */
@Mapper
public interface AssignmentMapper {

    @Select("SELECT a.*, c.name AS course_name, " +
            "(SELECT COUNT(*) FROM submission s WHERE s.assignment_id = a.id) AS submission_count, " +
            "(SELECT COUNT(*) FROM submission s INNER JOIN grade g ON s.id = g.submission_id WHERE s.assignment_id = a.id) AS graded_count " +
            "FROM assignment a LEFT JOIN course c ON a.course_id = c.id WHERE a.id = #{id}")
    Assignment findById(Long id);

    @Select("SELECT a.*, c.name AS course_name, " +
            "(SELECT COUNT(*) FROM submission s WHERE s.assignment_id = a.id AND s.student_id = #{studentId}) AS submission_count, " +
            "(SELECT COUNT(*) FROM submission s INNER JOIN grade g ON s.id = g.submission_id WHERE s.assignment_id = a.id AND s.student_id = #{studentId}) AS graded_count " +
            "FROM assignment a LEFT JOIN course c ON a.course_id = c.id WHERE a.id = #{id}")
    Assignment findByIdWithStudent(@Param("id") Long id, @Param("studentId") Long studentId);

    @Select("<script>" +
            "SELECT a.*, c.name AS course_name, " +
            "(SELECT COUNT(*) FROM submission s WHERE s.assignment_id = a.id) AS submission_count, " +
            "(SELECT COUNT(*) FROM submission s INNER JOIN grade g ON s.id = g.submission_id WHERE s.assignment_id = a.id) AS graded_count " +
            "FROM assignment a LEFT JOIN course c ON a.course_id = c.id WHERE 1=1" +
            "<if test='courseId != null'> AND a.course_id = #{courseId}</if>" +
            "<if test='status != null'> AND a.status = #{status}</if>" +
            "ORDER BY a.created_at DESC" +
            "</script>")
    List<Assignment> findAll(@Param("courseId") Long courseId, @Param("status") Integer status);

    @Select("SELECT a.*, c.name AS course_name, " +
            "(SELECT COUNT(*) FROM submission s WHERE s.assignment_id = a.id AND s.student_id = #{studentId}) AS submission_count, " +
            "(SELECT COUNT(*) FROM submission s INNER JOIN grade g ON s.id = g.submission_id WHERE s.assignment_id = a.id AND s.student_id = #{studentId}) AS graded_count " +
            "FROM assignment a LEFT JOIN course c ON a.course_id = c.id " +
            "WHERE a.course_id IN (SELECT course_id FROM course_student WHERE student_id = #{studentId}) " +
            "ORDER BY a.created_at DESC")
    List<Assignment> findByStudentId(Long studentId);

    @Insert("INSERT INTO assignment (course_id, title, description, deadline, max_score, file_types, max_size) " +
            "VALUES (#{courseId}, #{title}, #{description}, #{deadline}, #{maxScore}, #{fileTypes}, #{maxSize})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Assignment assignment);

    @Update("<script>" +
            "UPDATE assignment SET" +
            "<if test='title != null'> title = #{title},</if>" +
            "<if test='description != null'> description = #{description},</if>" +
            "<if test='deadline != null'> deadline = #{deadline},</if>" +
            "<if test='maxScore != null'> max_score = #{maxScore},</if>" +
            "<if test='fileTypes != null'> file_types = #{fileTypes},</if>" +
            "<if test='maxSize != null'> max_size = #{maxSize},</if>" +
            "<if test='status != null'> status = #{status},</if>" +
            "updated_at = NOW() WHERE id = #{id}" +
            "</script>")
    int update(Assignment assignment);

    @Delete("DELETE FROM assignment WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM assignment WHERE course_id = #{courseId}")
    Long countByCourseId(Long courseId);
}
