package com.courseassignment.service;

import com.courseassignment.entity.Notification;

import java.util.List;

/**
 * 通知服务接口
 */
public interface NotificationService {

    /**
     * 根据ID查询通知
     */
    Notification findById(Long id);

    /**
     * 获取用户的通知列表
     */
    List<Notification> findByUserId(Long userId);

    /**
     * 获取用户未读通知数
     */
    Long countUnread(Long userId);

    /**
     * 标记通知为已读
     */
    void markAsRead(Long id);

    /**
     * 全部标记为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 发送通知
     */
    void sendNotification(Long userId, String title, String content, String type);

    /**
     * 删除通知
     */
    void deleteById(Long id);
}
