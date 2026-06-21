<template>
  <div class="grades-page">
    <h2 class="page-title" style="margin-bottom:16px;">成绩管理</h2>

    <el-table :data="grades" v-loading="loading" stripe>
      <el-table-column prop="assignmentTitle" label="作业标题" min-width="180" />
      <el-table-column v-if="userStore.isTeacher" prop="studentName" label="学生" width="100" />
      <el-table-column prop="score" label="得分" width="100">
        <template #default="{ row }">
          <span style="font-weight:bold;font-size:16px;" :style="{ color: row.score >= 90 ? '#67c23a' : row.score >= 60 ? '#e6a23c' : '#f56c6c' }">
            {{ row.score }}分
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="comment" label="评语" min-width="200" show-overflow-tooltip />
      <el-table-column v-if="userStore.isTeacher" prop="gradedByName" label="批改教师" width="120" />
      <el-table-column prop="gradedTime" label="批改时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.gradedTime) }}
        </template>
      </el-table-column>
      <el-table-column v-if="userStore.isTeacher" label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showEditDialog(row)">修改</el-button>
          <el-button size="small" type="danger" @click="deleteGrade(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改分数对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改成绩" width="400px">
      <el-form label-width="80px">
        <el-form-item label="学生">{{ editingGrade?.studentName }}</el-form-item>
        <el-form-item label="作业">{{ editingGrade?.assignmentTitle }}</el-form-item>
        <el-form-item label="得分">
          <el-input-number v-model="editForm.score" :min="0" :max="100" :step="0.5" />
        </el-form-item>
        <el-form-item label="评语">
          <el-input v-model="editForm.comment" type="textarea" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { gradeApi } from '@/api/grade'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const grades = ref([])
const loading = ref(false)
const editDialogVisible = ref(false)
const editingGrade = ref(null)
const saving = ref(false)
const editForm = ref({ score: 0, comment: '' })

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

function showEditDialog(row) {
  editingGrade.value = row
  editForm.value = { score: row.score, comment: row.comment || '' }
  editDialogVisible.value = true
}

async function saveEdit() {
  saving.value = true
  try {
    await gradeApi.updateGrade(editingGrade.value.id, editForm.value)
    ElMessage.success('成绩修改成功')
    editDialogVisible.value = false
    fetchGrades()
  } catch (e) { /* ignore */ }
  saving.value = false
}

async function deleteGrade(id) {
  try {
    await ElMessageBox.confirm('确定要删除该成绩记录吗？', '确认', { type: 'warning' })
    await gradeApi.deleteGrade(id)
    ElMessage.success('删除成功')
    fetchGrades()
  } catch (e) { /* cancel */ }
}

async function fetchGrades() {
  loading.value = true
  try {
    const res = userStore.isTeacher
      ? await gradeApi.getMyTeacherGrades()
      : await gradeApi.getMyGrades()
    grades.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
}

onMounted(fetchGrades)
</script>

<style scoped>
.grades-page h2 { font-size: 20px; color: #303133; margin-bottom: 16px; }
</style>
