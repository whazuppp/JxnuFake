<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  window.open('https://jxnu.fanya.chaoxing.com/portal', '_blank')
  router.push('/studenthome') // 可自定义跳转回哪个页面
})
</script>

<template>
  <div>正在跳转到网络教学平台...</div>
</template>
