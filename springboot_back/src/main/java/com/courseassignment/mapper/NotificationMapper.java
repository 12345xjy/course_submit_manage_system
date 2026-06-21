package com.courseassignment.mapper;

import com.courseassignment.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 通知Mapper接口
 */
@Mapper
public interface NotificationMapper {

    @Select("SELECT * FROM notification WHERE id = #{id}")
    Notification findById(Long id);

    @Select("SELECT * FROM notification WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Notification> findByUserId(Long userId);

    @Select("SELECT * FROM notification WHERE user_id = #{userId} AND is_read = 0 ORDER BY created_at DESC")
    List<Notification> findUnreadByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM notification WHERE user_id = #{userId} AND is_read = 0")
    Long countUnreadByUserId(Long userId);

    @Insert("INSERT INTO notification (user_id, title, content, type, is_read) " +
            "VALUES (#{userId}, #{title}, #{content}, #{type}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notification notification);

    @Update("UPDATE notification SET is_read = 1 WHERE id = #{id}")
    int markAsRead(Long id);

    @Update("UPDATE notification SET is_read = 1 WHERE user_id = #{userId}")
    int markAllAsRead(Long userId);

    @Delete("DELETE FROM notification WHERE id = #{id}")
    int deleteById(Long id);
}
