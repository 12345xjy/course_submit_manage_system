import request from '@/utils/request'

export const gradeApi = {
  getGrade(id) {
    return request.get(`/grades/${id}`)
  },
  getGradeBySubmission(submissionId) {
    return request.get(`/grades/submission/${submissionId}`)
  },
  // 教师
  gradeSubmission(data) {
    return request.post('/teacher/grades', data)
  },
  getGradesByAssignment(assignmentId) {
    return request.get('/teacher/grades', { params: { assignmentId } })
  },
  updateGrade(id, data) {
    return request.put(`/teacher/grades/${id}`, data)
  },
  deleteGrade(id) {
    return request.delete(`/teacher/grades/${id}`)
  },
  // 学生
  getMyGrades() {
    return request.get('/student/grades')
  }
}
