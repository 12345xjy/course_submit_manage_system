<template>
  <div class="assignments-page">
    <div class="page-toolbar">
      <h2>作业管理</h2>
      <el-button v-if="userStore.isTeacher" type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon> 发布作业
      </el-button>
    </div>

    <!-- 筛选 -->
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="8">
        <el-select v-model="filterCourseId" placeholder="按课程筛选" clearable @change="fetchAssignments" style="width:100%">
          <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="filterStatus" placeholder="状态" clearable @change="fetchAssignments" style="width:100%">
          <el-option label="进行中" :value="1" />
          <el-option label="已关闭" :value="0" />
        </el-select>
      </el-col>
    </el-row>

    <!-- 作业列表 -->
    <el-table :data="assignments" v-loading="loading" stripe>
      <el-table-column prop="title" label="作业标题" min-width="200" />
      <el-table-column prop="courseName" label="所属课程" width="160" />
      <el-table-column prop="deadline" label="截止时间" width="180">
        <template #default="{ row }">
          <span :class="{ 'text-danger': new Date(row.deadline) < new Date() }">
            {{ formatDate(row.deadline) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="maxScore" label="满分" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '进行中' : '已关闭' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交/批改" width="120">
        <template #default="{ row }">
          {{ row.submissionCount || 0 }}/{{ row.gradedCount || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/assignments/${row.id}`)">详情</el-button>
          <template v-if="userStore.isTeacher">
            <el-button size="small" @click="editAssignment(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteAssignment(row.id)">删除</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 创建/编辑作业对话框 -->
    <el-dialog v-model="showCreateDialog" :title="editingAssignment ? '编辑作业' : '发布作业'" width="600px">
      <el-form :model="assignmentForm" label-width="100px">
        <el-form-item label="所属课程" required>
          <el-select v-model="assignmentForm.courseId" placeholder="请选择课程" style="width:100%">
            <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="作业标题" required>
          <el-input v-model="assignmentForm.title" placeholder="请输入作业标题" />
        </el-form-item>
        <el-form-item label="作业描述">
          <el-input v-model="assignmentForm.description" type="textarea" rows="4" placeholder="请输入作业描述和要求" />
        </el-form-item>
        <el-form-item label="截止时间" required>
          <el-date-picker v-model="assignmentForm.deadline" type="datetime" placeholder="选择截止时间"
            value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="满分分值">
          <el-input-number v-model="assignmentForm.maxScore" :min="0" :max="200" :step="1" />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-input v-model="assignmentForm.fileTypes" placeholder="如: .pdf,.doc,.zip（逗号分隔）" />
        </el-form-item>
        <el-form-item label="大小限制(MB)">
          <el-input-number v-model="assignmentForm.maxSize" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="saveAssignment">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { assignmentApi } from '@/api/assignment'
import { courseApi } from '@/api/course'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const userStore = useUserStore()
const assignments = ref([])
const courses = ref([])
const loading = ref(false)
const filterCourseId = ref(null)
const filterStatus = ref(null)
const showCreateDialog = ref(false)
const editingAssignment = ref(null)

const assignmentForm = ref({
  courseId: null, title: '', description: '',
  deadline: '', maxScore: 100, fileTypes: '', maxSize: 10
})

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

async function fetchCourses() {
  try {
    const res = userStore.isTeacher
      ? await courseApi.getMyTeachingCourses()
      : await courseApi.getAllCourses()
    courses.value = res.data
  } catch (e) { /* ignore */ }
}

async function fetchAssignments() {
  loading.value = true
  try {
    const params = {}
    if (filterCourseId.value) params.courseId = filterCourseId.value
    if (filterStatus.value !== null && filterStatus.value !== '') params.status = filterStatus.value
    const res = userStore.isStudent
      ? await assignmentApi.getMyAssignments()
      : await assignmentApi.getAllAssignments(params)
    assignments.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
}

function editAssignment(assignment) {
  editingAssignment.value = assignment
  assignmentForm.value = { ...assignment }
  showCreateDialog.value = true
}

async function saveAssignment() {
  try {
    if (editingAssignment.value) {
      await assignmentApi.updateAssignment(editingAssignment.value.id, assignmentForm.value)
      ElMessage.success('作业更新成功')
    } else {
      await assignmentApi.createAssignment(assignmentForm.value)
      ElMessage.success('作业发布成功')
    }
    showCreateDialog.value = false
    editingAssignment.value = null
    assignmentForm.value = {
      courseId: null, title: '', description: '',
      deadline: '', maxScore: 100, fileTypes: '', maxSize: 10
    }
    fetchAssignments()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

async function deleteAssignment(id) {
  try {
    await ElMessageBox.confirm('确定要删除该作业吗？', '确认删除', { type: 'warning' })
    await assignmentApi.deleteAssignment(id)
    ElMessage.success('作业删除成功')
    fetchAssignments()
  } catch (e) { /* cancel */ }
}

onMounted(() => {
  fetchCourses()
  fetchAssignments()
})
</script>

<style scoped>
.page-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.page-toolbar h2 { font-size: 20px; color: #303133; }
.text-danger { color: #f56c6c; font-weight: 500; }
</style>
