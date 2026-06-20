package com.courseassignment.mapper;

import com.courseassignment.entity.Submission;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作业提交Mapper接口
 */
@Mapper
public interface SubmissionMapper {

    @Select("SELECT s.*, u.real_name AS student_name, u.student_no, a.title AS assignment_title, c.name AS course_name, " +
            "g.score, g.comment " +
            "FROM submission s " +
            "LEFT JOIN user u ON s.student_id = u.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "LEFT JOIN course c ON a.course_id = c.id " +
            "LEFT JOIN grade g ON s.id = g.submission_id " +
            "WHERE s.id = #{id}")
    Submission findById(Long id);

    @Select("SELECT s.*, u.real_name AS student_name, u.student_no, a.title AS assignment_title, c.name AS course_name, " +
            "g.score, g.comment " +
            "FROM submission s " +
            "LEFT JOIN user u ON s.student_id = u.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "LEFT JOIN course c ON a.course_id = c.id " +
            "LEFT JOIN grade g ON s.id = g.submission_id " +
            "WHERE s.assignment_id = #{assignmentId} " +
            "ORDER BY s.submit_time DESC")
    List<Submission> findByAssignmentId(Long assignmentId);

    @Select("SELECT s.*, u.real_name AS student_name, u.student_no, a.title AS assignment_title, c.name AS course_name, " +
            "g.score, g.comment " +
            "FROM submission s " +
            "LEFT JOIN user u ON s.student_id = u.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "LEFT JOIN course c ON a.course_id = c.id " +
            "LEFT JOIN grade g ON s.id = g.submission_id " +
            "WHERE s.student_id = #{studentId} " +
            "ORDER BY s.submit_time DESC")
    List<Submission> findByStudentId(Long studentId);

    @Select("SELECT s.*, u.real_name AS student_name, u.student_no, a.title AS assignment_title, c.name AS course_name, " +
            "g.score, g.comment " +
            "FROM submission s " +
            "LEFT JOIN user u ON s.student_id = u.id " +
            "LEFT JOIN assignment a ON s.assignment_id = a.id " +
            "LEFT JOIN course c ON a.course_id = c.id " +
            "LEFT JOIN grade g ON s.id = g.submission_id " +
            "WHERE s.assignment_id = #{assignmentId} AND s.student_id = #{studentId}")
    Submission findByAssignmentAndStudent(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);

    @Insert("INSERT INTO submission (assignment_id, student_id, file_name, file_path, file_size, is_late, status) " +
            "VALUES (#{assignmentId}, #{studentId}, #{fileName}, #{filePath}, #{fileSize}, #{isLate}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Submission submission);

    @Update("UPDATE submission SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM submission WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM submission WHERE assignment_id = #{assignmentId}")
    Long countByAssignmentId(Long assignmentId);

    @Select("SELECT COUNT(*) FROM submission WHERE assignment_id = #{assignmentId} AND student_id = #{studentId}")
    Long countByAssignmentAndStudent(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);
}
