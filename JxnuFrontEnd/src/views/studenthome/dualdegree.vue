<script setup>
import { onMounted } from 'vue'

onMounted(() => {
  alert('对不起，未找到你双学位报名记录!')
})
</script>

<template>
  <div class="top-bar">
    对不起，未找到你双学位报名记录!
  </div>
</template>

<style scoped>
.top-bar {
  width: 100%;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
}
</style>