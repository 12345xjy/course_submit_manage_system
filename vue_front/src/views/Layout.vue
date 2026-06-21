<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '68px' : '230px'" class="layout-aside">
      <div class="logo-area">
        <span class="logo-icon">📚</span>
        <transition name="fade">
          <span v-if="!isCollapse" class="logo-text">作业管理系统</span>
        </transition>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        class="side-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>首页概览</template>
        </el-menu-item>

        <template v-if="userStore.isStudent">
          <el-menu-item index="/courses"><el-icon><Reading /></el-icon><template #title>课程列表</template></el-menu-item>
          <el-menu-item index="/assignments"><el-icon><Document /></el-icon><template #title>我的作业</template></el-menu-item>
          <el-menu-item index="/submissions"><el-icon><Upload /></el-icon><template #title>提交记录</template></el-menu-item>
          <el-menu-item index="/grades"><el-icon><Trophy /></el-icon><template #title>成绩查询</template></el-menu-item>
        </template>

        <template v-if="userStore.isTeacher">
          <el-menu-item index="/courses"><el-icon><Reading /></el-icon><template #title>课程管理</template></el-menu-item>
          <el-menu-item index="/assignments"><el-icon><Document /></el-icon><template #title>作业管理</template></el-menu-item>
          <el-menu-item index="/grades"><el-icon><Trophy /></el-icon><template #title>成绩管理</template></el-menu-item>
        </template>

        <template v-if="userStore.isAdmin">
          <el-menu-item index="/admin/users"><el-icon><User /></el-icon><template #title>用户管理</template></el-menu-item>
          <el-menu-item index="/courses"><el-icon><Reading /></el-icon><template #title>课程管理</template></el-menu-item>
        </template>

        <el-menu-item index="/notifications">
          <el-icon><Bell /></el-icon>
          <template #title>
            消息通知
            <el-badge v-if="unreadCount > 0" :value="unreadCount" class="menu-badge" />
          </template>
        </el-menu-item>

        <el-menu-item index="/profile"><el-icon><Setting /></el-icon><template #title>个人中心</template></el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse" :size="22">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="pageTitle">{{ pageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-tooltip content="消息通知" placement="bottom">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99" class="header-badge">
              <el-icon :size="20" class="header-icon" @click="$router.push('/notifications')"><Bell /></el-icon>
            </el-badge>
          </el-tooltip>
          <el-dropdown trigger="click">
            <span class="user-dropdown">
              <el-avatar :size="32" icon="UserFilled" class="user-avatar" />
              <span class="user-name">{{ userStore.realName }}</span>
              <el-icon class="dropdown-arrow"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/profile')">
                  <el-icon><Setting /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="page-fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
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
  Bell, Setting, Fold, Expand, User, ArrowDown, SwitchButton
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)
const unreadCount = ref(0)

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '')

function toggleCollapse() { isCollapse.value = !isCollapse.value }

function handleLogout() { userStore.logout(); router.push('/login') }

onMounted(() => {
  fetchUnreadCount()
  setInterval(fetchUnreadCount, 30000)
})

async function fetchUnreadCount() {
  try { const res = await notificationApi.getUnreadCount(); unreadCount.value = res.data.count } catch (e) { /* ignore */ }
}
</script>

<style scoped>
/* ===== 布局容器 ===== */
.layout-container { height: 100vh; }

/* ===== 侧边栏 ===== */
.layout-aside {
  background: linear-gradient(180deg, #1e2740 0%, #26304d 50%, #1a2340 100%);
  overflow: hidden;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.logo-area {
  height: 64px; display: flex; align-items: center; justify-content: center;
  gap: 10px; border-bottom: 1px solid rgba(255,255,255,0.06);
}
.logo-icon { font-size: 26px; flex-shrink: 0; }
.logo-text { font-size: 16px; font-weight: 700; color: #e8ecf4; letter-spacing: 1px; white-space: nowrap; }

/* ===== 菜单 ===== */
.side-menu { border-right: none !important; background: transparent !important; }
.side-menu :deep(.el-menu-item) {
  height: 48px; line-height: 48px; margin: 2px 10px; border-radius: 10px;
  color: #b0bdd4; font-size: 14px; transition: all 0.25s;
}
.side-menu :deep(.el-menu-item:hover) {
  background: rgba(255,255,255,0.06) !important; color: #e8ecf4;
}
.side-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, rgba(91,127,255,0.25), rgba(140,158,255,0.15)) !important;
  color: #ffffff !important; font-weight: 600;
  box-shadow: 0 2px 8px rgba(91,127,255,0.15);
}
.side-menu :deep(.el-menu-item .el-icon) { font-size: 18px; }
.menu-badge { margin-left: 6px; }

/* ===== 主内容区 ===== */
.main-container { background: #f0f2f5; }

/* ===== 顶栏 ===== */
.layout-header {
  height: 60px; background: #ffffff;
  border-bottom: 1px solid #ebeef2;
  display: flex; justify-content: space-between; align-items: center;
  padding: 0 24px; box-shadow: 0 1px 6px rgba(0,0,0,0.03);
  z-index: 10;
}
.header-left { display: flex; align-items: center; gap: 16px; }
.collapse-btn {
  cursor: pointer; color: #86909c; padding: 6px; border-radius: 8px;
  transition: all 0.2s;
}
.collapse-btn:hover { background: #f2f3f5; color: #5b7fff; }
.header-right { display: flex; align-items: center; gap: 20px; }

.header-badge :deep(.el-badge__content) { background: #ff4d4f; }
.header-icon { color: #4e5969; cursor: pointer; transition: color 0.2s; }
.header-icon:hover { color: #5b7fff; }

.user-dropdown {
  display: flex; align-items: center; gap: 8px; cursor: pointer; padding: 4px 8px; border-radius: 8px; transition: background 0.2s;
}
.user-dropdown:hover { background: #f7f8fa; }
.user-avatar { background: linear-gradient(135deg, #5b7fff, #8c9eff) !important; }
.user-name { font-size: 14px; color: #1d2129; font-weight: 500; }
.dropdown-arrow { color: #86909c; font-size: 12px; transition: transform 0.2s; }

/* ===== 主内容区 ===== */
.layout-main { padding: 24px; overflow-y: auto; background: #f0f2f5; min-height: calc(100vh - 60px); }

/* ===== 过渡动画 ===== */
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.page-fade-enter-active, .page-fade-leave-active { transition: all 0.25s ease; }
.page-fade-enter-from { opacity: 0; transform: translateY(10px); }
.page-fade-leave-to { opacity: 0; transform: translateY(-10px); }
</style>
