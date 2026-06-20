package com.courseassignment.controller;

import com.courseassignment.common.Result;
import com.courseassignment.entity.Notification;
import com.courseassignment.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 获取当前用户的通知列表
     */
    @GetMapping
    public Result<List<Notification>> getNotifications(@RequestAttribute("userId") Long userId) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        return Result.success(notifications);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    public Result<Map<String, Long>> getUnreadCount(@RequestAttribute("userId") Long userId) {
        Long count = notificationService.countUnread(userId);
        return Result.success(Map.of("count", count));
    }

    /**
     * 标记通知为已读
     */
    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return Result.success();
    }

    /**
     * 全部标记为已读
     */
    @PutMapping("/read-all")
    public Result<Void> markAllAsRead(@RequestAttribute("userId") Long userId) {
        notificationService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
        return Result.success();
    }
}
