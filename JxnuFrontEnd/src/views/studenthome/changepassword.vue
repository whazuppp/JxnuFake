<template>
  <div class="change-password-container">
    <h2>修改密码</h2>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="原密码" prop="oldPassword">
        <el-input
          v-model="form.oldPassword"
          type="password"
          autocomplete="off"
          show-password
        />
      </el-form-item>

      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="form.newPassword"
          type="password"
          autocomplete="off"
          show-password
        />
      </el-form-item>

      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          autocomplete="off"
          show-password
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="submitForm">
          提交修改
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { changePasswordApi } from '@/api/stu'

const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'))
    return
  }
  callback()
}

const rules = {
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

const submitForm = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const result = await changePasswordApi({
      oldPassword: form.oldPassword,
      newPassword: form.newPassword
    })

    if (result.code === 1) {
      ElMessage.success('密码修改成功，请重新登录')
      localStorage.removeItem('loginUser')
      router.push('/login')
    } else {
      ElMessage.error(result.msg || '修改失败')
    }
  } catch (err) {
    ElMessage.error('请求出错，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.change-password-container {
  max-width: 400px;
  margin: 100px auto;
}
</style>
