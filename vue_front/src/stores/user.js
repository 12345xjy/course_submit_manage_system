import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const realName = ref(localStorage.getItem('realName') || '')
  const role = ref(localStorage.getItem('role') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 'ADMIN')
  const isTeacher = computed(() => role.value === 'TEACHER' || role.value === 'ADMIN')
  const isStudent = computed(() => role.value === 'STUDENT')

  function setUserInfo(data) {
    token.value = data.token
    userId.value = data.userId
    username.value = data.username
    realName.value = data.realName
    role.value = data.role

    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', data.userId)
    localStorage.setItem('username', data.username)
    localStorage.setItem('realName', data.realName)
    localStorage.setItem('role', data.role)
  }

  async function login(loginData) {
    const res = await authApi.login(loginData)
    setUserInfo(res.data)
    return res
  }

  async function register(registerData) {
    const res = await authApi.register(registerData)
    return res
  }

  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    realName.value = ''
    role.value = ''

    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('realName')
    localStorage.removeItem('role')
  }

  return {
    token, userId, username, realName, role,
    isLoggedIn, isAdmin, isTeacher, isStudent,
    login, register, logout, setUserInfo
  }
})
