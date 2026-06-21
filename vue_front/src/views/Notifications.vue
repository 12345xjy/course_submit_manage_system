<template>
  <div class="notifications-page">
    <div class="page-toolbar">
      <h2 class="page-title">消息通知</h2>
      <el-button type="primary" text @click="markAllRead">全部标为已读</el-button>
    </div>

    <el-table :data="notifications" v-loading="loading" stripe>
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.type === 'ASSIGNMENT'" type="warning" size="small">作业</el-tag>
          <el-tag v-else-if="row.type === 'GRADE'" type="success" size="small">成绩</el-tag>
          <el-tag v-else type="info" size="small">系统</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag v-if="!row.isRead" type="danger" size="small" effect="dark">未读</el-tag>
          <span v-else style="color:#909399;">已读</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="!row.isRead" size="small" type="primary" text @click="readOne(row.id)">标为已读</el-button>
          <el-button size="small" type="danger" text @click="deleteOne(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { notificationApi } from '@/api/notification'
import { ElMessage, ElMessageBox } from 'element-plus'

const notifications = ref([])
const loading = ref(false)

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

async function fetchNotifications() {
  loading.value = true
  try {
    const res = await notificationApi.getNotifications()
    notifications.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
}

async function readOne(id) {
  await notificationApi.markAsRead(id)
  fetchNotifications()
}

async function markAllRead() {
  await notificationApi.markAllAsRead()
  fetchNotifications()
  ElMessage.success('全部标记为已读')
}

async function deleteOne(id) {
  try {
    await ElMessageBox.confirm('确定要删除该通知吗？', '确认', { type: 'info' })
    await notificationApi.deleteNotification(id)
    fetchNotifications()
  } catch (e) { /* cancel */ }
}

onMounted(fetchNotifications)
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
