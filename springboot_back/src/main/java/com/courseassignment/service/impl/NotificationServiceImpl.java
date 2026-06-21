package com.courseassignment.service.impl;

import com.courseassignment.entity.Notification;
import com.courseassignment.mapper.NotificationMapper;
import com.courseassignment.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知服务实现类
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Notification findById(Long id) {
        return notificationMapper.findById(id);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notificationMapper.findByUserId(userId);
    }

    @Override
    public Long countUnread(Long userId) {
        return notificationMapper.countUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long id) {
        notificationMapper.markAsRead(id);
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }

    @Override
    public void sendNotification(Long userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(0);
        notificationMapper.insert(notification);
    }

    @Override
    public void deleteById(Long id) {
        notificationMapper.deleteById(id);
    }
}
