package com.courseassignment.mapper;

import com.courseassignment.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 通知Mapper接口 —— 所有SQL手写于 NotificationMapper.xml
 */
@Mapper
public interface NotificationMapper {

    Notification findById(Long id);

    List<Notification> findByUserId(Long userId);

    List<Notification> findUnreadByUserId(Long userId);

    Long countUnreadByUserId(Long userId);

    int insert(Notification notification);

    int markAsRead(Long id);

    int markAllAsRead(Long userId);

    int deleteById(Long id);
}
