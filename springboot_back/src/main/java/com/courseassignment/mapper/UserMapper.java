package com.courseassignment.mapper;

import com.courseassignment.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE student_no = #{studentNo}")
    User findByStudentNo(String studentNo);

    @Select("SELECT * FROM user WHERE teacher_no = #{teacherNo}")
    User findByTeacherNo(String teacherNo);

    @Select("<script>" +
            "SELECT * FROM user WHERE 1=1" +
            "<if test='role != null and role != \"\"'> AND role = #{role}</if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (real_name LIKE CONCAT('%',#{keyword},'%') OR username LIKE CONCAT('%',#{keyword},'%'))</if>" +
            "ORDER BY created_at DESC" +
            "</script>")
    List<User> findAll(@Param("role") String role, @Param("keyword") String keyword);

    @Select("SELECT * FROM user WHERE role = 'TEACHER' AND status = 1")
    List<User> findTeachers();

    @Insert("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email, avatar) " +
            "VALUES (#{username}, #{password}, #{realName}, #{role}, #{studentNo}, #{teacherNo}, #{phone}, #{email}, #{avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("<script>" +
            "UPDATE user SET" +
            "<if test='realName != null'> real_name = #{realName},</if>" +
            "<if test='phone != null'> phone = #{phone},</if>" +
            "<if test='email != null'> email = #{email},</if>" +
            "<if test='avatar != null'> avatar = #{avatar},</if>" +
            "<if test='status != null'> status = #{status},</if>" +
            "<if test='password != null and password != \"\"'> password = #{password},</if>" +
            "updated_at = NOW() WHERE id = #{id}" +
            "</script>")
    int update(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM user WHERE role = #{role}")
    Long countByRole(@Param("role") String role);
}
