import request from '@/utils/request'

export const assignmentApi = {
  getAllAssignments(params) {
    return request.get('/assignments', { params })
  },
  getAssignment(id) {
    return request.get(`/assignments/${id}`)
  },
  // 教师
  createAssignment(data) {
    return request.post('/teacher/assignments', data)
  },
  updateAssignment(id, data) {
    return request.put(`/teacher/assignments/${id}`, data)
  },
  deleteAssignment(id) {
    return request.delete(`/teacher/assignments/${id}`)
  },
  // 学生
  getMyAssignments() {
    return request.get('/student/assignments')
  }
}
