<template>
  <div class="profile-page">
    <h2 class="page-title" style="margin-bottom:16px;">个人中心</h2>

    <el-tabs v-model="activeTab">
      <!-- 个人信息 -->
      <el-tab-pane label="个人信息" name="info">
        <el-card>
          <el-form :model="profileForm" label-width="100px" style="max-width:500px;">
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled />
            </el-form-item>
            <el-form-item label="真实姓名">
              <el-input v-model="profileForm.realName" />
            </el-form-item>
            <el-form-item label="角色">
              <el-tag>{{ roleText }}</el-tag>
            </el-form-item>
            <el-form-item v-if="profile.studentNo" label="学号">
              <el-input v-model="profileForm.studentNo" disabled />
            </el-form-item>
            <el-form-item v-if="profile.teacherNo" label="工号">
              <el-input v-model="profileForm.teacherNo" disabled />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="saving" @click="updateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 修改密码 -->
      <el-tab-pane label="修改密码" name="password">
        <el-card>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px" style="max-width:400px;">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="changingPwd" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('info')
const profile = ref({})
const saving = ref(false)
const changingPwd = ref(false)
const passwordFormRef = ref(null)

const profileForm = reactive({
  username: '', realName: '', studentNo: '', teacherNo: '', phone: '', email: ''
})

const passwordForm = reactive({
  oldPassword: '', newPassword: '', confirmPassword: ''
})

const roleText = computed(() => {
  const map = { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }
  return map[userStore.role] || ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

async function fetchProfile() {
  try {
    const res = await userApi.getProfile()
    profile.value = res.data
    Object.assign(profileForm, res.data)
  } catch (e) { /* ignore */ }
}

async function updateProfile() {
  saving.value = true
  try {
    await userApi.updateProfile({
      realName: profileForm.realName,
      phone: profileForm.phone,
      email: profileForm.email
    })
    ElMessage.success('个人信息更新成功')
  } catch (e) { /* ignore */ }
  saving.value = false
}

async function changePassword() {
  const valid = await passwordFormRef.value.validate().catch(() => false)
  if (!valid) return

  changingPwd.value = true
  try {
    await userApi.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码修改成功')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (e) { /* ignore */ }
  changingPwd.value = false
}

onMounted(fetchProfile)
</script>

<style scoped>
.profile-page h2 { font-size: 20px; color: #303133; margin-bottom: 16px; }
</style>
