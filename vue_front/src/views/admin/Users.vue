<template>
  <div class="admin-users">
    <div class="page-toolbar">
      <h2>用户管理</h2>
    </div>

    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="6">
        <el-select v-model="filterRole" placeholder="角色筛选" clearable @change="fetchUsers" style="width:100%">
          <el-option label="学生" value="STUDENT" />
          <el-option label="教师" value="TEACHER" />
          <el-option label="管理员" value="ADMIN" />
        </el-select>
      </el-col>
      <el-col :span="8">
        <el-input v-model="keyword" placeholder="搜索用户名/姓名" clearable @input="fetchUsers" :prefix-icon="Search" />
      </el-col>
    </el-row>

    <el-table :data="users" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="130" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : row.role === 'TEACHER' ? 'warning' : 'success'" size="small">
            {{ row.role === 'ADMIN' ? '管理员' : row.role === 'TEACHER' ? '教师' : '学生' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="studentNo" label="学号/工号" width="140">
        <template #default="{ row }">
          {{ row.studentNo || row.teacherNo || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'"
            @click="toggleStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
          <el-button size="small" type="danger" @click="deleteUser(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const users = ref([])
const loading = ref(false)
const filterRole = ref('')
const keyword = ref('')

async function fetchUsers() {
  loading.value = true
  try {
    const res = await userApi.getAllUsers({ role: filterRole.value, keyword: keyword.value })
    users.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
}

async function toggleStatus(user) {
  try {
    const newStatus = user.status === 1 ? 0 : 1
    await userApi.toggleUserStatus(user.id, newStatus)
    ElMessage.success(`用户${newStatus === 1 ? '启用' : '禁用'}成功`)
    fetchUsers()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

async function deleteUser(id) {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？此操作不可恢复！', '确认删除', { type: 'warning' })
    await userApi.deleteUser(id)
    ElMessage.success('用户删除成功')
    fetchUsers()
  } catch (e) { /* cancel */ }
}

onMounted(fetchUsers)
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
