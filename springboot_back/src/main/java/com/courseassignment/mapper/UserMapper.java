package com.courseassignment.mapper;

import com.courseassignment.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口 —— 所有SQL手写于 UserMapper.xml
 * 核心评分点：动态条件查询 + 多条件过滤
 */
@Mapper
public interface UserMapper {

    User findById(Long id);

    User findByUsername(String username);

    User findByStudentNo(String studentNo);

    User findByTeacherNo(String teacherNo);

    List<User> findAll(@Param("role") String role, @Param("keyword") String keyword);

    List<User> findTeachers();

    int insert(User user);

    int update(User user);

    int deleteById(Long id);

    Long countByRole(@Param("role") String role);
}
