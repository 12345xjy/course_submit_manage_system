import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

// 创建 Axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 防重复弹窗标记
let lastErrorTime = 0
let lastErrorMessage = ''

function showErrorOnce(msg) {
  const now = Date.now()
  // 相同消息 3 秒内不重复弹出
  if (msg !== lastErrorMessage || now - lastErrorTime > 3000) {
    lastErrorTime = now
    lastErrorMessage = msg
    ElMessage.error(msg)
  }
}

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 业务层 code 非 200
    if (res.code !== undefined && res.code !== 200) {
      showErrorOnce(res.message || '请求失败')
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    // HTTP 状态码错误
    if (error.response) {
      const { status } = error.response
      if (status === 401) {
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
        // 401 不弹"网络错误"，由业务层静默处理
        return Promise.reject(error)
      } else if (status === 403) {
        // 403 无权限，不弹窗（避免登录页反复弹）
        console.warn('权限不足:', error.config?.url)
        return Promise.reject(error)
      } else if (status >= 500) {
        showErrorOnce('服务器繁忙，请稍后重试')
      } else if (status === 404) {
        // 404 静默，不弹窗
        return Promise.reject(error)
      } else {
        showErrorOnce('请求失败，请稍后重试')
      }
    } else if (error.code === 'ECONNABORTED') {
      showErrorOnce('请求超时，请检查网络')
    } else if (!error.response) {
      // 真正的网络错误（无响应）
      showErrorOnce('网络连接失败，请检查网络')
    }
    return Promise.reject(error)
  }
)

export default request
