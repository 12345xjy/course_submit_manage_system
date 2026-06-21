<template>
  <div class="submissions-page">
    <h2 class="page-title" style="margin-bottom:16px;">提交记录</h2>

    <el-table :data="submissions" v-loading="loading" stripe>
      <el-table-column prop="assignmentTitle" label="作业标题" min-width="180" />
      <el-table-column prop="courseName" label="所属课程" width="160" />
      <el-table-column v-if="userStore.isTeacher" prop="studentName" label="学生" width="100" />
      <el-table-column prop="fileName" label="文件名" min-width="180">
        <template #default="{ row }">
          <el-button type="primary" link @click="downloadFile(row.filePath)">{{ row.fileName }}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.submitTime) }}
          <el-tag v-if="row.isLate" type="danger" size="small">迟交</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'GRADED' ? 'success' : 'warning'" size="small">
            {{ row.status === 'GRADED' ? '已批改' : '已提交' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="成绩" width="100">
        <template #default="{ row }">
          <span v-if="row.score != null" style="color:#67c23a;font-weight:bold;">{{ row.score }}分</span>
          <span v-else style="color:#909399;">-</span>
        </template>
      </el-table-column>
      <el-table-column v-if="userStore.isTeacher" label="操作" width="100">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="deleteSub(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { submissionApi } from '@/api/submission'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const submissions = ref([])
const loading = ref(false)

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

function downloadFile(filePath) {
  const url = submissionApi.getDownloadUrl(filePath)
  window.open(url, '_blank')
}

async function deleteSub(id) {
  try {
    await ElMessageBox.confirm('确定要删除该提交记录吗？', '确认', { type: 'warning' })
    await submissionApi.deleteSubmission(id)
    ElMessage.success('删除成功')
    fetchSubmissions()
  } catch (e) { /* cancel */ }
}

async function fetchSubmissions() {
  loading.value = true
  try {
    const res = await submissionApi.getMySubmissions()
    submissions.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
}

onMounted(fetchSubmissions)
</script>

<style scoped>
.submissions-page h2 { font-size: 20px; color: #303133; margin-bottom: 16px; }
</style>
