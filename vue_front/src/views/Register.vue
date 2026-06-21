<template>
  <div class="register-wrapper">
    <div class="bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>

    <div class="register-container">
      <div class="register-brand">
        <div class="brand-icon">✨</div>
        <h1 class="brand-title">加入我们</h1>
        <p class="brand-desc">创建账号，开始管理课程作业</p>
        <div class="brand-features">
          <div class="feature-item">
            <span class="feature-dot"></span> 学生端：提交作业，查看成绩
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span> 教师端：布置作业，在线批改
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span> 管理员：统筹全局，数据管理
          </div>
        </div>
      </div>

      <div class="register-card">
        <div class="card-header">
          <h2>创建账号</h2>
          <p>请填写以下信息完成注册</p>
        </div>

        <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item prop="username">
                <el-input v-model="form.username" placeholder="用户名" size="large" class="custom-input" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="realName">
                <el-input v-model="form.realName" placeholder="真实姓名" size="large" class="custom-input" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item prop="password">
                <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password class="custom-input" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="confirmPassword">
                <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large" show-password class="custom-input" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item prop="role">
            <el-radio-group v-model="form.role" class="role-group">
              <el-radio-button value="STUDENT">🎓 学生</el-radio-button>
              <el-radio-button value="TEACHER">👨‍🏫 教师</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item v-if="form.role === 'STUDENT'" prop="studentNo">
            <el-input v-model="form.studentNo" placeholder="学号" size="large" class="custom-input" />
          </el-form-item>
          <el-form-item v-if="form.role === 'TEACHER'" prop="teacherNo">
            <el-input v-model="form.teacherNo" placeholder="工号" size="large" class="custom-input" />
          </el-form-item>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item prop="phone">
                <el-input v-model="form.phone" placeholder="手机号（选填）" size="large" class="custom-input" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="email">
                <el-input v-model="form.email" placeholder="邮箱（选填）" size="large" class="custom-input" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="handleRegister" class="register-btn">
              {{ loading ? '注册中...' : '注 册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="register-footer">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '', realName: '', password: '', confirmPassword: '',
  role: 'STUDENT', studentNo: '', teacherNo: '', phone: '', email: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次输入的密码不一致'))
  else callback()
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirmPassword, trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.register({
      username: form.username, password: form.password, realName: form.realName,
      role: form.role,
      studentNo: form.role === 'STUDENT' ? form.studentNo : null,
      teacherNo: form.role === 'TEACHER' ? form.teacherNo : null,
      phone: form.phone, email: form.email
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) { /* 拦截器已处理 */ }
  finally { loading.value = false }
}
</script>

<style scoped>
.register-wrapper {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(160deg, #eef2ff 0%, #e8f0fe 30%, #f0f5ff 60%, #f5f7fa 100%);
  overflow: hidden;
}
.bg-shapes { position: absolute; inset: 0; pointer-events: none; z-index: 0; }
.shape { position: absolute; border-radius: 50%; opacity: 0.12; filter: blur(60px); }
.shape-1 { width: 500px; height: 500px; background: #5b7fff; top: -15%; right: -10%; animation: float1 20s ease-in-out infinite; }
.shape-2 { width: 400px; height: 400px; background: #a78bfa; bottom: -10%; left: -8%; animation: float2 25s ease-in-out infinite; }
.shape-3 { width: 300px; height: 300px; background: #67c23a; top: 60%; left: 50%; opacity: 0.06; animation: float3 18s ease-in-out infinite; }
@keyframes float1 { 0%,100% { transform: translate(0,0) scale(1); } 33% { transform: translate(40px,-30px) scale(1.05); } 66% { transform: translate(-20px,20px) scale(0.95); } }
@keyframes float2 { 0%,100% { transform: translate(0,0) scale(1); } 50% { transform: translate(-30px,-20px) scale(1.08); } }
@keyframes float3 { 0%,100% { transform: translate(0,0) scale(1); } 50% { transform: translate(25px,15px) scale(0.9); } }

.register-container {
  position: relative; z-index: 1;
  display: flex; width: 960px; min-height: 600px;
  background: rgba(255,255,255,0.85); backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 4px 24px rgba(91,127,255,0.08), 0 20px 60px rgba(0,0,0,0.06);
  overflow: hidden;
}
.register-brand {
  flex: 1; background: linear-gradient(160deg, #5b7fff 0%, #7b93ff 50%, #8c9eff 100%);
  padding: 50px 40px; display: flex; flex-direction: column; justify-content: center;
  color: #fff; position: relative; overflow: hidden;
}
.register-brand::before { content:''; position:absolute; width:300px; height:300px; background:rgba(255,255,255,0.08); border-radius:50%; top:-60px; right:-60px; }
.register-brand::after { content:''; position:absolute; width:200px; height:200px; background:rgba(255,255,255,0.06); border-radius:50%; bottom:-40px; left:-40px; }
.brand-icon { font-size:48px; margin-bottom:16px; position:relative; z-index:1; animation: bounce 3s ease-in-out infinite; }
@keyframes bounce { 0%,100% { transform:translateY(0); } 50% { transform:translateY(-8px); } }
.brand-title { font-size:26px; font-weight:700; margin:0 0 8px; letter-spacing:1px; position:relative; z-index:1; }
.brand-desc { font-size:13px; opacity:0.85; margin-bottom:36px; letter-spacing:1px; position:relative; z-index:1; }
.brand-features { display:flex; flex-direction:column; gap:14px; position:relative; z-index:1; }
.feature-item { font-size:14px; opacity:0.9; display:flex; align-items:center; gap:8px; }
.feature-dot { width:6px; height:6px; background:rgba(255,255,255,0.8); border-radius:50%; flex-shrink:0; }

.register-card { flex: 1.2; padding: 40px 36px; display:flex; flex-direction:column; justify-content:center; overflow-y:auto; }
.card-header { margin-bottom:24px; }
.card-header h2 { font-size:24px; font-weight:700; color:#1d2129; margin:0 0 6px; }
.card-header p { font-size:14px; color:#86909c; margin:0; }

.register-form { width:100%; }
.custom-input :deep(.el-input__wrapper) { border-radius:10px !important; padding:4px 14px; background:#f7f8fa; border:1px solid transparent !important; transition:all 0.25s; }
.custom-input :deep(.el-input__wrapper:hover) { background:#f2f3f5; border-color:#e5e6eb !important; }
.custom-input :deep(.el-input__wrapper.is-focus) { background:#fff; border-color:#5b7fff !important; box-shadow:0 0 0 3px rgba(91,127,255,0.1) !important; }

.role-group { width:100%; }
.role-group :deep(.el-radio-button__inner) { border-radius:8px !important; padding:10px 28px; border:none !important; background:#f2f3f5; color:#4e5969; font-weight:500; transition:all 0.25s; }
.role-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) { background: linear-gradient(135deg, #5b7fff, #7b93ff); color:#fff; box-shadow:0 2px 12px rgba(91,127,255,0.3); }

.register-btn { width:100%; height:46px; border-radius:10px !important; font-size:16px; font-weight:600; letter-spacing:2px; background:linear-gradient(135deg,#5b7fff,#7b93ff) !important; border:none !important; box-shadow:0 4px 16px rgba(91,127,255,0.3); transition:all 0.3s; }
.register-btn:hover { box-shadow:0 6px 24px rgba(91,127,255,0.45); transform:translateY(-1px); }
.register-footer { text-align:center; color:#86909c; font-size:14px; margin-top:8px; }
.register-footer a { color:#5b7fff; text-decoration:none; font-weight:500; }

@media (max-width:768px) { .register-container { flex-direction:column; width:94%; } .register-brand { padding:30px 24px; } .register-card { padding:30px 24px; } }
</style>
