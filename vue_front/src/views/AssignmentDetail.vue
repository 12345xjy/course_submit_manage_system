<template>
  <div class="assignment-detail" v-loading="loading">
    <el-page-header @back="$router.back()">
      <template #content>
        <span class="page-title">{{ assignment.title }}</span>
      </template>
    </el-page-header>

    <el-descriptions :column="2" border class="info-block" v-if="assignment.id">
      <el-descriptions-item label="所属课程">{{ assignment.courseName }}</el-descriptions-item>
      <el-descriptions-item label="满分">{{ assignment.maxScore }}分</el-descriptions-item>
      <el-descriptions-item label="截止时间">
        <span :class="{ 'text-danger': new Date(assignment.deadline) < new Date() }">
          {{ formatDate(assignment.deadline) }}
        </span>
      </el-descriptions-item>
      <el-descriptions-item label="允许类型">{{ assignment.fileTypes || '不限' }}</el-descriptions-item>
      <el-descriptions-item label="作业描述" :span="2">
        {{ assignment.description || '暂无描述' }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 学生: 提交作业 -->
    <div v-if="userStore.isStudent" class="submit-section">
      <h3>提交作业</h3>
      <div v-if="mySubmission">
        <el-alert title="您已提交该作业" type="success" :closable="false" show-icon>
          <template #default>
            提交时间：{{ formatDate(mySubmission.submitTime) }}
            <span v-if="mySubmission.isLate" style="color:#f56c6c;margin-left:12px;">(迟交)</span>
          </template>
        </el-alert>
        <div style="margin-top: 8px;">
          <span>文件：{{ mySubmission.fileName }}</span>
          <el-button size="small" type="primary" link @click="downloadFile(mySubmission.filePath)">
            下载
          </el-button>
        </div>
        <div v-if="mySubmission.score != null" style="margin-top: 8px; color: #67c23a;">
          得分：{{ mySubmission.score }}分
          <span v-if="mySubmission.comment"> | 评语：{{ mySubmission.comment }}</span>
        </div>
      </div>
      <div v-else>
        <el-upload
          ref="uploadRef"
          :action="''"
          :auto-upload="false"
          :limit="1"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
        >
          <el-button type="primary">选择文件</el-button>
          <template #tip>
            <div class="el-upload__tip">支持格式：{{ assignment.fileTypes || '不限' }}，最大{{ assignment.maxSize }}MB</div>
          </template>
        </el-upload>
        <el-button type="success" :loading="submitting" @click="handleSubmit" style="margin-top: 12px;">
          提交作业
        </el-button>
      </div>
    </div>

    <!-- 教师: 提交列表与批改 -->
    <div v-if="userStore.isTeacher" class="submissions-section">
      <h3>学生提交列表</h3>
      <el-table :data="submissions" stripe>
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="studentNo" label="学号" width="140" />
        <el-table-column prop="fileName" label="提交文件" min-width="180">
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
        <el-table-column label="成绩" width="120">
          <template #default="{ row }">
            <span v-if="row.score != null">{{ row.score }}分</span>
            <el-tag v-else type="warning" size="small">未批改</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="showGradeDialog(row)">
              {{ row.score != null ? '修改评分' : '批改' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 批改对话框 -->
    <el-dialog v-model="gradeDialogVisible" title="作业批改" width="450px">
      <el-form label-width="80px">
        <el-form-item label="学生">
          {{ gradingSubmission?.studentName }} ({{ gradingSubmission?.studentNo }})
        </el-form-item>
        <el-form-item label="得分" required>
          <el-input-number v-model="gradeForm.score" :min="0" :max="assignment.maxScore" :step="0.5" />
          <span style="margin-left:8px;color:#909399;">/ {{ assignment.maxScore }}分</span>
        </el-form-item>
        <el-form-item label="评语">
          <el-input v-model="gradeForm.comment" type="textarea" rows="3" placeholder="请输入评语（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="grading" @click="submitGrade">确认评分</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { assignmentApi } from '@/api/assignment'
import { submissionApi } from '@/api/submission'
import { gradeApi } from '@/api/grade'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const assignment = ref({})
const submissions = ref([])
const mySubmission = ref(null)
const loading = ref(false)
const submitting = ref(false)
const selectedFile = ref(null)
const uploadRef = ref(null)
const gradeDialogVisible = ref(false)
const gradingSubmission = ref(null)
const grading = ref(false)

const gradeForm = ref({ score: 0, comment: '' })

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

function downloadFile(filePath) {
  const url = submissionApi.getDownloadUrl(filePath)
  window.open(url, '_blank')
}

function handleFileChange(file) {
  selectedFile.value = file.raw
}

function handleFileRemove() {
  selectedFile.value = null
}

async function handleSubmit() {
  if (!selectedFile.value) {
    ElMessage.warning('请选择要提交的文件')
    return
  }
  submitting.value = true
  try {
    const formData = new FormData()
    formData.append('assignmentId', assignment.value.id)
    formData.append('file', selectedFile.value)
    await submissionApi.submitAssignment(formData)
    ElMessage.success('作业提交成功')
    fetchData()
  } catch (e) {
    ElMessage.error(e.message || '提交失败')
  }
  submitting.value = false
}

function showGradeDialog(submission) {
  gradingSubmission.value = submission
  gradeForm.value = {
    score: submission.score || 0,
    comment: submission.comment || ''
  }
  gradeDialogVisible.value = true
}

async function submitGrade() {
  grading.value = true
  try {
    const data = {
      submissionId: gradingSubmission.value.id,
      score: gradeForm.value.score,
      comment: gradeForm.value.comment
    }
    if (gradingSubmission.value.score != null) {
      // 查找已存在的成绩ID
      const gradeRes = await gradeApi.getGradeBySubmission(gradingSubmission.value.id)
      if (gradeRes.data) {
        await gradeApi.updateGrade(gradeRes.data.id, data)
      } else {
        await gradeApi.gradeSubmission(data)
      }
    } else {
      await gradeApi.gradeSubmission(data)
    }
    ElMessage.success('评分成功')
    gradeDialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error(e.message || '评分失败')
  }
  grading.value = false
}

async function fetchData() {
  loading.value = true
  try {
    const id = route.params.id
    const assignmentRes = await assignmentApi.getAssignment(id)
    assignment.value = assignmentRes.data

    if (userStore.isTeacher) {
      const submissionsRes = await submissionApi.getSubmissionsByAssignment(id)
      submissions.value = submissionsRes.data
    }

    if (userStore.isStudent) {
      const submissionsRes = await submissionApi.getMySubmissions()
      mySubmission.value = submissionsRes.data.find(s => s.assignmentId === parseInt(id)) || null
    }
  } catch (e) { /* ignore */ }
  loading.value = false
}

onMounted(fetchData)
</script>

<style scoped>
.assignment-detail { padding: 0; }
.page-title { font-size: 18px; color: #303133; }
.info-block { margin: 20px 0; }
.submit-section, .submissions-section { margin-top: 24px; }
.submit-section h3, .submissions-section h3 { margin-bottom: 12px; font-size: 16px; }
.text-danger { color: #f56c6c; font-weight: 500; }
</style>
