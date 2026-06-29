<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  window.open('http://jwc.jxnu.edu.cn/weike/default.aspx', '_blank')
  router.push('/studenthome') // 可根据需要跳转页面
})
</script>

<template>
  <div>正在跳转到正大微课...</div>
</template>
