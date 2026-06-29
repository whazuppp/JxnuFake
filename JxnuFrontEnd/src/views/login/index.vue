<script setup>
import { ref } from 'vue'
import {loginApi} from '@/api/login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

let loginForm = ref({ stuId:'', password:''})
let router = useRouter()

const clear = () => {
  loginForm.value = {
    stuId: '',
    password: ''
  }
}

const login = async () => {
  const result = await loginApi(loginForm.value)
  if (result.code) {
    ElMessage.success('登录成功')
    localStorage.setItem('loginUser', JSON.stringify(result.data))
    router.push('/')
  } else {
    ElMessage.error(result.msg)
  }
}
</script>

<template>
  <div class="login-container">
    <!-- 左侧图片区域 - 修改为更合理的图片展示方式 -->
    <div class="left-section">
      <img src="@/assets/login-bg.png" alt="校园背景图" class="login-image">
    </div>
    
    <!-- 右侧登录区域 -->
    <div class="right-section">
      <div class="login-content">
        <div class="login-box">
          <div class="login-title">教务在线</div>
          <div class="login-subtitle">账号密码登录</div>
          
          <el-form class="login-form">
            <el-form-item>
              <el-input 
  v-model="loginForm.stuId" 
  placeholder="请输入工号或学号"
  class="custom-input"
/>

            </el-form-item>
            
            <el-form-item>
              <el-input 
                type="password" 
                v-model="loginForm.password" 
                placeholder="请输入密码"
                class="custom-input"
              ></el-input>
            </el-form-item>

            <el-form-item class="login-buttons">
              <el-button type="primary" @click="login" class="login-btn">登录</el-button>
            </el-form-item>
          </el-form>
          
          <div class="login-links">
            <div class="links-left">
              <a href="#">下载APP</a>
              <a href="#">激活账号</a>
              <a href="#">忘记密码？</a>
            </div>
            <div class="links-right">
              <span>其他方式登录</span>
            </div>
          </div>
        </div>
        
        <div class="browser-login">
          <span>浏览器登录：</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  width: 100%;
  min-height: 100vh;
  display: flex;
}

.left-section {
  width: 60%;
  position: relative;
  background-color: #f0f2f5;
  overflow: hidden;
}
.login-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.right-section {
  width: 40%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-content {
  width: 100%;
  max-width: 400px;
  padding: 0 40px;
}

.login-box {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
  color: #333;
}

.login-subtitle {
  font-size: 16px;
  text-align: center;
  margin-bottom: 30px;
  color: #666;
}

.custom-input {
  margin-bottom: 20px;
}

.login-buttons {
  margin-top: 30px;
}

.login-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.login-links {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 14px;
}

.links-left a {
  color: #1890ff;
  margin-right: 15px;
  text-decoration: none;
}

.links-left a:hover {
  text-decoration: underline;
}

.links-right {
  color: #666;
}

.browser-login {
  margin-top: 30px;
  color: #666;
  font-size: 14px;
  text-align: center;
}
</style>