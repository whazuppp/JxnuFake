<template>
  <div class="welcome-message">
    欢迎您登录，{{ loginName }}同学，您可以访问【学生之家】
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const loginName = ref('')

onMounted(() => {
  const userJson = localStorage.getItem('loginUser') // 修正为 loginUser
  try {
    const user = JSON.parse(userJson)
    if (user && user.name) {
      loginName.value = user.name
    } else {
      console.warn('未找到用户信息或用户姓名为空')
    }
  } catch (e) {
    console.error('解析用户信息出错', e)
  }
})
</script>

<style scoped>
.welcome-message {
  color: red;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  margin-top: 200px;
}
</style>
