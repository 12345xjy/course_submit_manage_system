package com.courseassignment.mapper;

import com.courseassignment.entity.Grade;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 成绩/批改Mapper接口
 */
@Mapper
public interface GradeMapper {

    @Select("SELECT g.*, u.real_name AS graded_by_name, su.real_name AS student_name, a.title AS assignment_title " +
            "FROM grade g " +
            "LEFT JOIN user u ON g.graded_by = u.id " +
            "LEFT JOIN submission s ON g.submission_id = s.id " +
            "LEFT JOIN user su ON s.student_id = su.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "WHERE g.id = #{id}")
    Grade findById(Long id);

    @Select("SELECT g.*, u.real_name AS graded_by_name, su.real_name AS student_name, a.title AS assignment_title " +
            "FROM grade g " +
            "LEFT JOIN user u ON g.graded_by = u.id " +
            "LEFT JOIN submission s ON g.submission_id = s.id " +
            "LEFT JOIN user su ON s.student_id = su.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "WHERE g.submission_id = #{submissionId}")
    Grade findBySubmissionId(Long submissionId);

    @Select("SELECT g.*, u.real_name AS graded_by_name, su.real_name AS student_name, a.title AS assignment_title, c.name AS course_name " +
            "FROM grade g " +
            "LEFT JOIN user u ON g.graded_by = u.id " +
            "LEFT JOIN submission s ON g.submission_id = s.id " +
            "LEFT JOIN user su ON s.student_id = su.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "LEFT JOIN course c ON a.course_id = c.id " +
            "WHERE s.assignment_id = #{assignmentId} " +
            "ORDER BY g.graded_time DESC")
    List<Grade> findByAssignmentId(Long assignmentId);

    @Select("<script>" +
            "SELECT g.*, u.real_name AS graded_by_name, su.real_name AS student_name, a.title AS assignment_title, c.name AS course_name " +
            "FROM grade g " +
            "LEFT JOIN user u ON g.graded_by = u.id " +
            "LEFT JOIN submission s ON g.submission_id = s.id " +
            "LEFT JOIN user su ON s.student_id = su.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "LEFT JOIN course c ON a.course_id = c.id " +
            "WHERE c.teacher_id = #{teacherId}" +
            "<if test='courseId != null'> AND a.course_id = #{courseId}</if>" +
            "ORDER BY g.graded_time DESC" +
            "</script>")
    List<Grade> findByTeacherId(@Param("teacherId") Long teacherId, @Param("courseId") Long courseId);

    @Select("SELECT g.*, u.real_name AS graded_by_name, su.real_name AS student_name, a.title AS assignment_title " +
            "FROM grade g " +
            "LEFT JOIN user u ON g.graded_by = u.id " +
            "LEFT JOIN submission s ON g.submission_id = s.id " +
            "LEFT JOIN user su ON s.student_id = su.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "WHERE s.student_id = #{studentId} " +
            "ORDER BY g.graded_time DESC")
    List<Grade> findByStudentId(Long studentId);

    @Insert("INSERT INTO grade (submission_id, score, comment, graded_by) " +
            "VALUES (#{submissionId}, #{score}, #{comment}, #{gradedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Grade grade);

    @Update("<script>" +
            "UPDATE grade SET" +
            "<if test='score != null'> score = #{score},</if>" +
            "<if test='comment != null'> comment = #{comment},</if>" +
            "updated_at = NOW() WHERE id = #{id}" +
            "</script>")
    int update(Grade grade);

    @Delete("DELETE FROM grade WHERE id = #{id}")
    int deleteById(Long id);
}
