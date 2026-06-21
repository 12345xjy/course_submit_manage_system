import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    meta: { requiresAuth: true },
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'courses',
        name: 'Courses',
        component: () => import('@/views/Courses.vue'),
        meta: { title: '课程管理', requiresTeacher: true }
      },
      {
        path: 'courses/:id',
        name: 'CourseDetail',
        component: () => import('@/views/CourseDetail.vue'),
        meta: { title: '课程详情', requiresTeacher: true }
      },
      {
        path: 'assignments',
        name: 'Assignments',
        component: () => import('@/views/Assignments.vue'),
        meta: { title: '作业管理', requiresTeacher: true }
      },
      {
        path: 'assignments/:id',
        name: 'AssignmentDetail',
        component: () => import('@/views/AssignmentDetail.vue'),
        meta: { title: '作业详情', requiresTeacher: true }
      },
      {
        path: 'submissions',
        name: 'Submissions',
        component: () => import('@/views/Submissions.vue'),
        meta: { title: '提交记录' }
      },
      {
        path: 'grades',
        name: 'Grades',
        component: () => import('@/views/Grades.vue'),
        meta: { title: '成绩管理' }
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: () => import('@/views/Notifications.vue'),
        meta: { title: '消息通知' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: 'admin/users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
        meta: { title: '用户管理', requiresAdmin: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token

  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
    return
  }

  if (to.meta.requiresAdmin && userStore.role !== 'ADMIN') {
    next('/dashboard')
    return
  }

  // 管理员不能访问教师专属页面
  if (to.meta.requiresTeacher && userStore.role === 'ADMIN') {
    next('/dashboard')
    return
  }

  if ((to.path === '/login' || to.path === '/register') && token) {
    next('/dashboard')
    return
  }

  next()
})

export default router
