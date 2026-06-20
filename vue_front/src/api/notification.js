import request from '@/utils/request'

export const notificationApi = {
  getNotifications() {
    return request.get('/notifications')
  },
  getUnreadCount() {
    return request.get('/notifications/unread-count')
  },
  markAsRead(id) {
    return request.put(`/notifications/${id}/read`)
  },
  markAllAsRead() {
    return request.put('/notifications/read-all')
  },
  deleteNotification(id) {
    return request.delete(`/notifications/${id}`)
  }
}
