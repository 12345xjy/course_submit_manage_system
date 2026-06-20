<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo">
        <span v-if="!isCollapse">📚 作业管理系统</span>
        <span v-else>📚</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>首页概览</template>
        </el-menu-item>

        <!-- 学生菜单 -->
        <template v-if="userStore.isStudent">
          <el-menu-item index="/courses">
            <el-icon><Reading /></el-icon>
            <template #title>课程列表</template>
          </el-menu-item>
          <el-menu-item index="/assignments">
            <el-icon><Document /></el-icon>
            <template #title>我的作业</template>
          </el-menu-item>
          <el-menu-item index="/submissions">
            <el-icon><Upload /></el-icon>
            <template #title>提交记录</template>
          </el-menu-item>
          <el-menu-item index="/grades">
            <el-icon><Trophy /></el-icon>
            <template #title>成绩查询</template>
          </el-menu-item>
        </template>

        <!-- 教师菜单 -->
        <template v-if="userStore.isTeacher">
          <el-menu-item index="/courses">
            <el-icon><Reading /></el-icon>
            <template #title>课程管理</template>
          </el-menu-item>
          <el-menu-item index="/assignments">
            <el-icon><Document /></el-icon>
            <template #title>作业管理</template>
          </el-menu-item>
          <el-menu-item index="/grades">
            <el-icon><Trophy /></el-icon>
            <template #title>成绩管理</template>
          </el-menu-item>
        </template>

        <!-- 管理员菜单 -->
        <template v-if="userStore.isAdmin">
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>
          <el-menu-item index="/courses">
            <el-icon><Reading /></el-icon>
            <template #title>课程管理</template>
          </el-menu-item>
        </template>

        <el-menu-item index="/notifications">
          <el-icon><Bell /></el-icon>
          <template #title>
            消息通知
            <el-badge v-if="unreadCount > 0" :value="unreadCount" class="badge" />
          </template>
        </el-menu-item>

        <el-menu-item index="/profile">
          <el-icon><Setting /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse" :size="20">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="page-title">{{ pageTitle }}</span>
        </div>
        <div class="header-right">
          <span class="user-info">
            {{ userStore.realName }} ({{ roleText }})
          </span>
          <el-button type="danger" text @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { notificationApi } from '@/api/notification'
import {
  DataAnalysis, Reading, Document, Upload, Trophy,
  Bell, Setting, Fold, Expand, User
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)
const unreadCount = ref(0)

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '')

const roleText = computed(() => {
  const map = { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }
  return map[userStore.role] || ''
})

function toggleCollapse() {
  isCollapse.value = !isCollapse.value
}

function handleLogout() {
  userStore.logout()
  router.push('/login')
}

async function fetchUnreadCount() {
  try {
    const res = await notificationApi.getUnreadCount()
    unreadCount.value = res.data.count
  } catch (e) { /* ignore */ }
}

onMounted(() => {
  fetchUnreadCount()
  setInterval(fetchUnreadCount, 30000)
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #304156;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.layout-header {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  cursor: pointer;
}

.page-title {
  font-size: 16px;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  color: #606266;
  font-size: 14px;
}

.layout-main {
  background: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}

.badge {
  margin-left: 8px;
}

.el-menu {
  border-right: none;
}
</style>
