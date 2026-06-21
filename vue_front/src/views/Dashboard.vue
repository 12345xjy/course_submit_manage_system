<template>
  <div class="dashboard">
    <!-- 欢迎区 -->
    <div class="welcome-section">
      <div class="welcome-text">
        <h2>👋 欢迎回来，{{ userStore.realName }}</h2>
        <p>{{ greetingText }}</p>
      </div>
      <div class="welcome-date">{{ currentDate }}</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6" v-for="card in statsCards" :key="card.label">
        <div class="stat-card" :style="{ '--accent': card.color }">
          <div class="stat-icon-box" :style="{ background: card.bg }">
            <el-icon :size="24" :color="card.color"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
          <div class="stat-trend">
            <span :style="{ color: card.color }">{{ card.trend }}</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作 + 系统信息 -->
    <el-row :gutter="16" class="bottom-row">
      <el-col :span="14">
        <el-card shadow="never" class="section-card">
          <template #header>
            <div class="card-header-wrap">
              <span class="card-title">⚡ 快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <div class="action-item" v-for="action in quickActions" :key="action.path"
              @click="$router.push(action.path)">
              <span class="action-icon">{{ action.emoji }}</span>
              <span class="action-label">{{ action.label }}</span>
              <el-icon class="action-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="section-card">
          <template #header>
            <div class="card-header-wrap">
              <span class="card-title">📋 系统公告</span>
            </div>
          </template>
          <div class="announcements">
            <div class="announce-item">
              <span class="announce-dot"></span>
              <span>欢迎使用高校课程作业提交与批改管理系统 v1.0</span>
            </div>
            <div class="announce-item">
              <span class="announce-dot"></span>
              <span>教师可发布作业，学生可在线提交，支持在线批改</span>
            </div>
            <div class="announce-item">
              <span class="announce-dot"></span>
              <span>系统已自动初始化测试数据，请前往各模块体验</span>
            </div>
            <div class="announce-item">
              <span class="announce-dot"></span>
              <span>如有问题，请联系系统管理员</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { courseApi } from '@/api/course'
import { assignmentApi } from '@/api/assignment'
import { submissionApi } from '@/api/submission'
import { gradeApi } from '@/api/grade'
import { Reading, Document, Upload, Trophy, DataAnalysis, ArrowRight } from '@element-plus/icons-vue'

const userStore = useUserStore()
const stats = ref({ courseCount: 0, assignmentCount: 0, submissionCount: 0, gradedCount: 0, teacherCourseCount: 0, teacherAssignmentCount: 0 })

const currentDate = computed(() => new Date().toLocaleDateString('zh-CN', { year:'numeric', month:'long', day:'numeric', weekday:'long' }))

const greetingText = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return '上午好！今天也是充满活力的一天 ☀️'
  if (h < 18) return '下午好！喝杯茶休息一下吧 🍵'
  return '晚上好！辛苦了，注意休息 🌙'
})

const statsCards = computed(() => {
  if (userStore.isStudent) return [
    { label: '已选课程', value: stats.value.courseCount, icon: Reading, color: '#5b7fff', bg: '#eef1ff', trend: '学习中' },
    { label: '待提交', value: stats.value.assignmentCount, icon: Document, color: '#fa8c16', bg: '#fff7e6', trend: '进行中' },
    { label: '已提交', value: stats.value.submissionCount, icon: Upload, color: '#52c41a', bg: '#f0fbe9', trend: '已提交' },
    { label: '已批改', value: stats.value.gradedCount, icon: Trophy, color: '#722ed1', bg: '#f9f0ff', trend: '已出分' },
  ]
  return [
    { label: '教授课程', value: stats.value.teacherCourseCount, icon: DataAnalysis, color: '#5b7fff', bg: '#eef1ff', trend: '进行中' },
    { label: '发布作业', value: stats.value.teacherAssignmentCount, icon: Document, color: '#fa8c16', bg: '#fff7e6', trend: '已发布' },
    { label: '待批改', value: stats.value.pendingGradeCount || 0, icon: Upload, color: '#ff4d4f', bg: '#fff0f0', trend: '待处理' },
    { label: '学生总数', value: stats.value.studentCount || 0, icon: Reading, color: '#52c41a', bg: '#f0fbe9', trend: '已选课' },
  ]
})

const quickActions = computed(() => {
  const actions = [
    { label: '课程管理', path: '/courses', emoji: '📖' },
    { label: '作业管理', path: '/assignments', emoji: '📝' },
    { label: '成绩查看', path: '/grades', emoji: '🏆' },
    { label: '消息通知', path: '/notifications', emoji: '🔔' },
  ]
  if (userStore.isAdmin) actions.unshift({ label: '用户管理', path: '/admin/users', emoji: '👥' })
  if (userStore.isStudent) actions.splice(2, 0, { label: '提交记录', path: '/submissions', emoji: '📤' })
  return actions
})

onMounted(async () => {
  try {
    if (userStore.isStudent) {
      const [c, a, s, g] = await Promise.all([courseApi.getMyCourses(), assignmentApi.getMyAssignments(), submissionApi.getMySubmissions(), gradeApi.getMyGrades()])
      stats.value.courseCount = c.data.length
      stats.value.assignmentCount = a.data.filter(x => x.status === 1).length
      stats.value.submissionCount = s.data.length
      stats.value.gradedCount = g.data.length
    } else if (userStore.isTeacher) {
      const [c, a] = await Promise.all([courseApi.getMyTeachingCourses(), assignmentApi.getAllAssignments()])
      stats.value.teacherCourseCount = c.data.length
      stats.value.teacherAssignmentCount = a.data.length
    }
  } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.dashboard { padding: 0; animation: fadeIn 0.4s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(8px); } to { opacity: 1; transform: translateY(0); } }

/* ===== 欢迎区 ===== */
.welcome-section {
  display: flex; justify-content: space-between; align-items: flex-start;
  margin-bottom: 24px; padding: 24px 28px;
  background: linear-gradient(135deg, #ffffff 0%, #fafbff 100%);
  border-radius: 16px; border: 1px solid #eef1ff;
  box-shadow: 0 2px 12px rgba(91,127,255,0.04);
}
.welcome-text h2 { font-size: 22px; font-weight: 700; color: #1d2129; margin: 0 0 6px; }
.welcome-text p { font-size: 14px; color: #86909c; margin: 0; }
.welcome-date { font-size: 14px; color: #5b7fff; font-weight: 500; white-space: nowrap; }

/* ===== 统计卡片 ===== */
.stats-row { margin-bottom: 16px; }
.stats-row .el-col { display: flex; }
.stat-card {
  --accent: #5b7fff;
  display: flex; align-items: center; gap: 14px;
  padding: 20px; background: #fff; border-radius: 14px;
  border: 1px solid #f0f1f5; transition: all 0.3s;
  cursor: pointer; position: relative; overflow: hidden;
  height: 96px; width: 100%; box-sizing: border-box;
}
.stat-card::after {
  content: ''; position: absolute; right: -20px; top: -20px;
  width: 80px; height: 80px; background: var(--accent);
  border-radius: 50%; opacity: 0.04; transition: all 0.3s;
}
.stat-card:hover { transform: translateY(-3px); box-shadow: 0 8px 24px rgba(0,0,0,0.06); border-color: #e5e6eb; }
.stat-card:hover::after { opacity: 0.08; width: 100px; height: 100px; }
.stat-icon-box {
  width: 48px; height: 48px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.stat-info { flex: 1; min-width: 0; }
.stat-value { font-size: 28px; font-weight: 700; color: #1d2129; line-height: 1.2; white-space: nowrap; }
.stat-label { font-size: 13px; color: #86909c; margin-top: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.stat-trend { font-size: 12px; font-weight: 500; flex-shrink: 0; opacity: 0.7; white-space: nowrap; }

/* ===== 下方区块 ===== */
.bottom-row { margin-top: 0; }
.section-card { border-radius: 14px !important; border: 1px solid #f0f1f5 !important; }
.card-header-wrap { display: flex; align-items: center; }
.card-title { font-size: 16px; font-weight: 600; color: #1d2129; }

.quick-actions { display: flex; flex-wrap: wrap; gap: 10px; }
.action-item {
  display: flex; align-items: center; gap: 10px; padding: 12px 18px;
  background: #f7f8fa; border-radius: 10px; cursor: pointer;
  transition: all 0.2s; flex: 1; min-width: 160px;
}
.action-item:hover { background: #eef1ff; transform: translateY(-1px); }
.action-icon { font-size: 18px; }
.action-label { font-size: 14px; color: #4e5969; font-weight: 500; flex: 1; }
.action-arrow { color: #c0c4cc; font-size: 14px; transition: transform 0.2s; }
.action-item:hover .action-arrow { transform: translateX(3px); color: #5b7fff; }

.announcements { display: flex; flex-direction: column; gap: 14px; }
.announce-item { display: flex; align-items: flex-start; gap: 10px; font-size: 14px; color: #4e5969; }
.announce-dot { width: 8px; height: 8px; background: #5b7fff; border-radius: 50%; flex-shrink: 0; margin-top: 4px; }
</style>
