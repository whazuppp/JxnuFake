<template>
  <section class="page">
    <h2>课程信息</h2>
    <el-form :inline="true" :model="filters">
      <el-form-item label="课程代码"><el-input v-model="filters.courseCode" clearable /></el-form-item>
      <el-form-item label="课程名称"><el-input v-model="filters.name" clearable /></el-form-item>
      <el-form-item label="课程类型"><el-select v-model="filters.courseType" clearable><el-option label="理论" value="THEORY" /><el-option label="实验" value="EXPERIMENT" /><el-option label="实践" value="PRACTICE" /></el-select></el-form-item>
      <el-button type="primary" @click="loadCourses">查询</el-button>
    </el-form>
    <el-table :data="courses" v-loading="loading" border stripe>
      <el-table-column prop="courseCode" label="课程代码" />
      <el-table-column prop="name" label="课程名称" min-width="180" />
      <el-table-column label="课程类型"><template #default="{ row }">{{ typeNames[row.courseType] || row.courseType }}</template></el-table-column>
      <el-table-column prop="credit" label="学分" />
      <el-table-column prop="weeklyPeriods" label="每周课时" />
    </el-table>
    <el-empty v-if="!loading && !courses.length" description="暂无课程数据" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryAllCoursesApi } from '@/api/course'
import { toOfferingParams } from '@/utils/offeringState'
const courses = ref([]), loading = ref(false)
const filters = reactive({ courseCode: '', name: '', courseType: '' })
const typeNames = { THEORY: '理论', EXPERIMENT: '实验', PRACTICE: '实践' }
async function loadCourses() {
  loading.value = true
  try {
    const result = await queryAllCoursesApi(toOfferingParams(filters))
    if (result.code !== 1) throw new Error(result.msg)
    courses.value = result.data || []
  } catch (error) { ElMessage.error(error.message || '课程列表加载失败') }
  finally { loading.value = false }
}
onMounted(loadCourses)
</script>

<style scoped>
.page { padding:20px; } h2 { margin-top:0; } .el-select { width:160px; }
</style>
