<template>
  <div class="login-wrapper">
    <!-- 背景装饰 -->
    <div class="bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <div class="login-container">
      <!-- 左侧品牌区 -->
      <div class="login-brand">
        <div class="brand-icon">📚</div>
        <h1 class="brand-title">课程作业管理系统</h1>
        <p class="brand-desc">Course Assignment Management System</p>
        <div class="brand-features">
          <div class="feature-item">
            <span class="feature-dot"></span> 在线提交 · 安全可靠
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span> 智能批改 · 高效便捷
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span> 数据统计 · 一目了然
          </div>
        </div>
      </div>

      <!-- 右侧登录卡片 -->
      <div class="login-card">
        <div class="card-header">
          <h2>欢迎回来</h2>
          <p>请登录您的账号以继续</p>
        </div>

        <el-form :model="form" ref="formRef" class="login-form">
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              size="large"
              class="custom-input"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              class="custom-input"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              @click="handleLogin"
              class="login-btn"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await userStore.login({ username: form.username, password: form.password })
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    console.error('Login error:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ===== 整体布局 ===== */
.login-wrapper {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(160deg, #eef2ff 0%, #e8f0fe 30%, #f0f5ff 60%, #f5f7fa 100%);
  overflow: hidden;
}

/* ===== 背景装饰形状 ===== */
.bg-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}
.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.12;
  filter: blur(60px);
}
.shape-1 {
  width: 500px; height: 500px;
  background: #5b7fff;
  top: -15%; right: -10%;
  animation: float1 20s ease-in-out infinite;
}
.shape-2 {
  width: 400px; height: 400px;
  background: #8c9eff;
  bottom: -10%; left: -8%;
  animation: float2 25s ease-in-out infinite;
}
.shape-3 {
  width: 300px; height: 300px;
  background: #a78bfa;
  top: 50%; left: 45%;
  animation: float3 18s ease-in-out infinite;
}
.shape-4 {
  width: 250px; height: 250px;
  background: #67c23a;
  top: 10%; left: 5%;
  opacity: 0.06;
  animation: float1 22s ease-in-out infinite reverse;
}
@keyframes float1 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(40px, -30px) scale(1.05); }
  66% { transform: translate(-20px, 20px) scale(0.95); }
}
@keyframes float2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(-30px, -20px) scale(1.08); }
}
@keyframes float3 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(25px, 15px) scale(0.9); }
}

/* ===== 主容器 ===== */
.login-container {
  position: relative;
  z-index: 1;
  display: flex;
  width: 900px;
  min-height: 520px;
  background: rgba(255,255,255,0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow:
    0 4px 24px rgba(91,127,255,0.08),
    0 20px 60px rgba(0,0,0,0.06);
  overflow: hidden;
}

/* ===== 左侧品牌区 ===== */
.login-brand {
  flex: 1;
  background: linear-gradient(160deg, #5b7fff 0%, #7b93ff 50%, #8c9eff 100%);
  padding: 50px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #fff;
  position: relative;
  overflow: hidden;
}
.login-brand::before {
  content: '';
  position: absolute;
  width: 300px; height: 300px;
  background: rgba(255,255,255,0.08);
  border-radius: 50%;
  top: -60px; right: -60px;
}
.login-brand::after {
  content: '';
  position: absolute;
  width: 200px; height: 200px;
  background: rgba(255,255,255,0.06);
  border-radius: 50%;
  bottom: -40px; left: -40px;
}
.brand-icon {
  font-size: 48px;
  margin-bottom: 16px;
  position: relative;
  z-index: 1;
  animation: bounce 3s ease-in-out infinite;
}
@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}
.brand-title {
  font-size: 26px;
  font-weight: 700;
  margin: 0 0 8px;
  letter-spacing: 1px;
  position: relative;
  z-index: 1;
}
.brand-desc {
  font-size: 13px;
  opacity: 0.85;
  margin-bottom: 36px;
  letter-spacing: 1px;
  position: relative;
  z-index: 1;
}
.brand-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
  position: relative;
  z-index: 1;
}
.feature-item {
  font-size: 14px;
  opacity: 0.9;
  display: flex;
  align-items: center;
  gap: 8px;
}
.feature-dot {
  width: 6px; height: 6px;
  background: rgba(255,255,255,0.8);
  border-radius: 50%;
  flex-shrink: 0;
}

/* ===== 右侧登录卡片 ===== */
.login-card {
  flex: 1;
  padding: 50px 44px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.card-header {
  margin-bottom: 32px;
}
.card-header h2 {
  font-size: 24px;
  font-weight: 700;
  color: #1d2129;
  margin: 0 0 8px;
}
.card-header p {
  font-size: 14px;
  color: #86909c;
  margin: 0;
}

.login-form {
  width: 100%;
}
.custom-input :deep(.el-input__wrapper) {
  border-radius: 10px !important;
  padding: 4px 14px;
  background: #f7f8fa;
  border: 1px solid transparent !important;
  transition: all 0.25s;
}
.custom-input :deep(.el-input__wrapper:hover) {
  background: #f2f3f5;
  border-color: #e5e6eb !important;
}
.custom-input :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  border-color: #5b7fff !important;
  box-shadow: 0 0 0 3px rgba(91,127,255,0.1) !important;
}
.custom-input :deep(.el-input__inner) {
  font-size: 15px;
}

.login-btn {
  width: 100%;
  height: 46px;
  border-radius: 10px !important;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #5b7fff, #7b93ff) !important;
  border: none !important;
  box-shadow: 0 4px 16px rgba(91,127,255,0.3);
  transition: all 0.3s;
}
.login-btn:hover {
  box-shadow: 0 6px 24px rgba(91,127,255,0.45);
  transform: translateY(-1px);
}

.login-footer {
  text-align: center;
  color: #86909c;
  font-size: 14px;
  margin-top: 8px;
}
.login-footer a {
  color: #5b7fff;
  text-decoration: none;
  font-weight: 500;
}
.login-footer a:hover {
  text-decoration: underline;
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    width: 94%;
    min-height: auto;
  }
  .login-brand {
    padding: 30px 24px;
  }
  .brand-title { font-size: 20px; }
  .login-card { padding: 30px 24px; }
}
</style>
