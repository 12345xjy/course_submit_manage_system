import request from '@/utils/request'

export const userApi = {
  getProfile() {
    return request.get('/user/profile')
  },
  updateProfile(data) {
    return request.put('/user/profile', data)
  },
  changePassword(data) {
    return request.put('/user/password', data)
  },
  getTeachers() {
    return request.get('/public/teachers')
  },
  // 管理员
  getAllUsers(params) {
    return request.get('/admin/users', { params })
  },
  toggleUserStatus(id, status) {
    return request.put(`/admin/users/${id}/status`, { status })
  },
  deleteUser(id) {
    return request.delete(`/admin/users/${id}`)
  }
}
