import request from '@/utils/request'

export const courseApi = {
  getAllCourses(params) {
    return request.get('/courses', { params })
  },
  getCourse(id) {
    return request.get(`/courses/${id}`)
  },
  // 教师
  createCourse(data) {
    return request.post('/teacher/courses', data)
  },
  updateCourse(id, data) {
    return request.put(`/teacher/courses/${id}`, data)
  },
  deleteCourse(id) {
    return request.delete(`/teacher/courses/${id}`)
  },
  getMyTeachingCourses() {
    return request.get('/teacher/courses')
  },
  getCourseStudents(courseId) {
    return request.get(`/teacher/courses/${courseId}/students`)
  },
  // 学生
  getMyCourses() {
    return request.get('/student/courses')
  },
  enrollCourse(courseId) {
    return request.post(`/student/courses/${courseId}/enroll`)
  },
  unenrollCourse(courseId) {
    return request.delete(`/student/courses/${courseId}/enroll`)
  }
}
