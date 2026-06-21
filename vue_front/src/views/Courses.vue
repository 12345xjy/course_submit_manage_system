<template>
  <div class="courses-page">
    <div class="page-toolbar">
      <h2 class="page-title">课程管理</h2>
      <el-button v-if="userStore.isTeacher" type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon> 创建课程
      </el-button>
    </div>

    <!-- 搜索 -->
    <el-input v-model="keyword" placeholder="搜索课程名称或编码" clearable @input="fetchCourses"
      style="width: 300px; margin-bottom: 16px;" :prefix-icon="Search" />

    <!-- 课程列表 -->
    <el-table :data="courses" v-loading="loading" stripe>
      <el-table-column prop="code" label="课程编码" width="120" />
      <el-table-column prop="name" label="课程名称" min-width="180" />
      <el-table-column prop="teacherName" label="授课教师" width="120" />
      <el-table-column prop="semester" label="学期" width="140" />
      <el-table-column prop="studentCount" label="学生人数" width="100" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '进行中' : '已结课' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/courses/${row.id}`)">详情</el-button>
          <template v-if="userStore.isStudent">
            <el-button v-if="!isEnrolled(row)" size="small" type="primary" @click="enroll(row.id)">选课</el-button>
            <el-button v-else size="small" type="danger" @click="unenroll(row.id)">退课</el-button>
          </template>
          <template v-if="userStore.isTeacher">
            <el-button size="small" @click="editCourse(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteCourse(row.id)">删除</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 创建/编辑课程对话框 -->
    <el-dialog v-model="showCreateDialog" :title="editingCourse ? '编辑课程' : '创建课程'" width="500px">
      <el-form :model="courseForm" label-width="80px">
        <el-form-item label="课程名称" required>
          <el-input v-model="courseForm.name" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程编码" required>
          <el-input v-model="courseForm.code" placeholder="如: CS301" :disabled="!!editingCourse" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="courseForm.description" type="textarea" rows="3" placeholder="请输入课程描述" />
        </el-form-item>
        <el-form-item label="学期" required>
          <el-input v-model="courseForm.semester" placeholder="如: 2025-2026-2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCourse">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { courseApi } from '@/api/course'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'

const userStore = useUserStore()
const courses = ref([])
const loading = ref(false)
const keyword = ref('')
const showCreateDialog = ref(false)
const editingCourse = ref(null)

const courseForm = ref({
  name: '', code: '', description: '', semester: '2025-2026-2'
})

async function fetchCourses() {
  loading.value = true
  try {
    const params = { keyword: keyword.value }
    if (userStore.isTeacher) {
      const res = await courseApi.getMyTeachingCourses()
      courses.value = res.data
    } else if (userStore.isAdmin) {
      const res = await courseApi.getAllCourses(params)
      courses.value = res.data
    } else {
      const res = await courseApi.getMyCourses()
      courses.value = res.data
    }
  } catch (e) { /* ignore */ }
  loading.value = false
}

function isEnrolled(course) {
  return courses.value.some(c => c.id === course.id)
}

async function enroll(courseId) {
  try {
    await courseApi.enrollCourse(courseId)
    ElMessage.success('选课成功')
    fetchCourses()
  } catch (e) {
    ElMessage.error(e.message || '选课失败')
  }
}

async function unenroll(courseId) {
  try {
    await ElMessageBox.confirm('确定要退选该课程吗？', '确认退课', { type: 'warning' })
    await courseApi.unenrollCourse(courseId)
    ElMessage.success('退课成功')
    fetchCourses()
  } catch (e) { /* cancel */ }
}

function editCourse(course) {
  editingCourse.value = course
  courseForm.value = { ...course }
  showCreateDialog.value = true
}

async function saveCourse() {
  try {
    if (editingCourse.value) {
      await courseApi.updateCourse(editingCourse.value.id, courseForm.value)
      ElMessage.success('课程更新成功')
    } else {
      await courseApi.createCourse(courseForm.value)
      ElMessage.success('课程创建成功')
    }
    showCreateDialog.value = false
    editingCourse.value = null
    courseForm.value = { name: '', code: '', description: '', semester: '2025-2026-2' }
    fetchCourses()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

async function deleteCourse(id) {
  try {
    await ElMessageBox.confirm('确定要删除该课程吗？此操作不可恢复！', '确认删除', { type: 'warning' })
    await courseApi.deleteCourse(id)
    ElMessage.success('课程删除成功')
    fetchCourses()
  } catch (e) { /* cancel */ }
}

onMounted(fetchCourses)
</script>

<style scoped>
.page-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.page-toolbar h2 { font-size: 20px; color: #303133; }
</style>
