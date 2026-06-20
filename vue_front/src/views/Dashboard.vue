<template>
  <div class="dashboard">
    <h2 class="page-header">欢迎，{{ userStore.realName }}</h2>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-if="userStore.isStudent">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background: #e6f7ff;">
              <el-icon :size="28" color="#1890ff"><Reading /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.courseCount }}</div>
              <div class="stats-label">已选课程</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6" v-if="userStore.isStudent">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background: #fff7e6;">
              <el-icon :size="28" color="#fa8c16"><Document /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.assignmentCount }}</div>
              <div class="stats-label">待提交作业</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6" v-if="userStore.isStudent">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background: #f6ffed;">
              <el-icon :size="28" color="#52c41a"><Upload /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.submissionCount }}</div>
              <div class="stats-label">已提交作业</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6" v-if="userStore.isStudent">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background: #f9f0ff;">
              <el-icon :size="28" color="#722ed1"><Trophy /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.gradedCount }}</div>
              <div class="stats-label">已批改作业</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <template v-if="userStore.isTeacher">
        <el-col :span="6">
          <el-card class="stats-card" shadow="hover">
            <div class="stats-content">
              <div class="stats-icon" style="background: #e6f7ff;">
                <el-icon :size="28" color="#1890ff"><Reading /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.teacherCourseCount }}</div>
                <div class="stats-label">教授课程</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card" shadow="hover">
            <div class="stats-content">
              <div class="stats-icon" style="background: #fff7e6;">
                <el-icon :size="28" color="#fa8c16"><Document /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.teacherAssignmentCount }}</div>
                <div class="stats-label">发布作业</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card" shadow="hover">
            <div class="stats-content">
              <div class="stats-icon" style="background: #f6ffed;">
                <el-icon :size="28" color="#52c41a"><Upload /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.pendingGradeCount }}</div>
                <div class="stats-label">待批改</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </template>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="20" class="quick-actions">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>📋 快捷操作</template>
          <el-space wrap>
            <el-button type="primary" @click="$router.push('/courses')">浏览课程</el-button>
            <el-button type="success" @click="$router.push('/assignments')">查看作业</el-button>
            <el-button type="warning" @click="$router.push('/grades')">查看成绩</el-button>
            <el-button type="info" @click="$router.push('/notifications')">查看通知</el-button>
          </el-space>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>💡 系统说明</template>
          <p>欢迎使用高校课程作业提交与批改管理系统！</p>
          <p>本系统支持教师发布作业、学生在线提交作业、教师在线批改评分等核心功能。</p>
          <p>如有问题，请联系系统管理员。</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { courseApi } from '@/api/course'
import { assignmentApi } from '@/api/assignment'
import { submissionApi } from '@/api/submission'
import { gradeApi } from '@/api/grade'
import { Reading, Document, Upload, Trophy } from '@element-plus/icons-vue'

const userStore = useUserStore()

const stats = ref({
  courseCount: 0,
  assignmentCount: 0,
  submissionCount: 0,
  gradedCount: 0,
  teacherCourseCount: 0,
  teacherAssignmentCount: 0,
  pendingGradeCount: 0
})

onMounted(async () => {
  if (userStore.isStudent) {
    try {
      const [coursesRes, assignmentsRes, submissionsRes, gradesRes] = await Promise.all([
        courseApi.getMyCourses(),
        assignmentApi.getMyAssignments(),
        submissionApi.getMySubmissions(),
        gradeApi.getMyGrades()
      ])
      stats.value.courseCount = coursesRes.data.length
      stats.value.assignmentCount = assignmentsRes.data.filter(a => a.status === 1).length
      stats.value.submissionCount = submissionsRes.data.length
      stats.value.gradedCount = gradesRes.data.length
    } catch (e) { /* ignore */ }
  }

  if (userStore.isTeacher) {
    try {
      const [coursesRes, assignmentsRes] = await Promise.all([
        courseApi.getMyTeachingCourses(),
        assignmentApi.getAllAssignments()
      ])
      stats.value.teacherCourseCount = coursesRes.data.length
      stats.value.teacherAssignmentCount = assignmentsRes.data.length
    } catch (e) { /* ignore */ }
  }
})
</script>

<style scoped>
.dashboard { padding: 0; }

.page-header { font-size: 20px; margin-bottom: 20px; color: #303133; }

.stats-row { margin-bottom: 20px; }

.stats-card { cursor: pointer; }
.stats-content { display: flex; align-items: center; gap: 16px; }
.stats-icon {
  width: 56px; height: 56px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
}
.stats-value { font-size: 24px; font-weight: bold; color: #303133; }
.stats-label { font-size: 13px; color: #909399; margin-top: 4px; }

.quick-actions p { color: #606266; font-size: 14px; line-height: 1.8; }
</style>
