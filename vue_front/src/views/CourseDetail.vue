<template>
  <div class="course-detail" v-loading="loading">
    <el-page-header @back="$router.back()">
      <template #content>
        <span class="page-title">{{ course.name }}</span>
      </template>
    </el-page-header>

    <el-descriptions :column="2" border class="course-info" v-if="course.id">
      <el-descriptions-item label="课程编码">{{ course.code }}</el-descriptions-item>
      <el-descriptions-item label="授课教师">{{ course.teacherName }}</el-descriptions-item>
      <el-descriptions-item label="学期">{{ course.semester }}</el-descriptions-item>
      <el-descriptions-item label="学生人数">{{ course.studentCount || 0 }}</el-descriptions-item>
      <el-descriptions-item label="课程描述" :span="2">{{ course.description || '暂无描述' }}</el-descriptions-item>
    </el-descriptions>

    <!-- 作业列表 -->
    <h3 style="margin: 20px 0 12px;">课程作业</h3>
    <el-table :data="assignments" stripe>
      <el-table-column prop="title" label="作业标题" min-width="200" />
      <el-table-column prop="deadline" label="截止时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.deadline) }}
        </template>
      </el-table-column>
      <el-table-column prop="maxScore" label="满分" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.submissionCount > 0 && userStore.isStudent" type="success">已完成</el-tag>
          <el-tag v-else-if="row.status === 1" type="warning">进行中</el-tag>
          <el-tag v-else type="info">已截止</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交情况" width="100">
        <template #default="{ row }">
          {{ row.submissionCount || 0 }}/{{ row.studentCount || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/assignments/${row.id}`)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 学生列表（教师可见）-->
    <template v-if="userStore.isTeacher">
      <h3 style="margin: 20px 0 12px;">选课学生</h3>
      <el-table :data="students" stripe>
        <el-table-column prop="studentNo" label="学号" width="150" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column prop="phone" label="手机号" width="150" />
      </el-table>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { courseApi } from '@/api/course'
import { assignmentApi } from '@/api/assignment'

const route = useRoute()
const userStore = useUserStore()
const course = ref({})
const assignments = ref([])
const students = ref([])
const loading = ref(false)

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

onMounted(async () => {
  loading.value = true
  try {
    const id = route.params.id
    const [courseRes, assignmentsRes] = await Promise.all([
      courseApi.getCourse(id),
      assignmentApi.getAllAssignments({ courseId: id })
    ])
    course.value = courseRes.data
    assignments.value = assignmentsRes.data

    if (userStore.isTeacher) {
      const studentsRes = await courseApi.getCourseStudents(id)
      students.value = studentsRes.data
    }
  } catch (e) { /* ignore */ }
  loading.value = false
})
</script>

<style scoped>
.course-detail { padding: 0; }
.page-title { font-size: 18px; color: #303133; }
.course-info { margin-top: 20px; }
</style>
