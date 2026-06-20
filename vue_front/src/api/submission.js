import request from '@/utils/request'

export const submissionApi = {
  getSubmission(id) {
    return request.get(`/submissions/${id}`)
  },
  // 学生
  submitAssignment(data) {
    return request.post('/student/submissions', data, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  getMySubmissions() {
    return request.get('/student/submissions')
  },
  // 教师
  getSubmissionsByAssignment(assignmentId) {
    return request.get('/teacher/submissions', { params: { assignmentId } })
  },
  // 通用
  deleteSubmission(id) {
    return request.delete(`/submissions/${id}`)
  },
  // 文件下载
  getDownloadUrl(filePath) {
    return `/api/files/download?filePath=${encodeURIComponent(filePath)}`
  }
}
