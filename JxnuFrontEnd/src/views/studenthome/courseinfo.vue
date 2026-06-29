<template>
  <div class="course-info-page">
    <div class="page-header">
      <h2>课程信息</h2>
      <el-button type="primary" :loading="loading" @click="loadCourses">
        刷新
      </el-button>
    </div>

    <el-table v-loading="loading" :data="courses" border stripe>
      <el-table-column prop="id" label="课程ID" width="90" />
      <el-table-column prop="name" label="课程名称" min-width="180" />
      <el-table-column prop="number" label="每周课时" width="110" />
      <el-table-column prop="teacherName" label="授课教师" width="140" />
      <el-table-column prop="createTime" label="创建时间" min-width="180" />
      <el-table-column prop="updateTime" label="更新时间" min-width="180" />
    </el-table>

    <el-empty v-if="!loading && courses.length === 0" description="暂无课程数据" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryAllCoursesApi } from '@/api/course'

const courses = ref([])
const loading = ref(false)

const loadCourses = async () => {
  loading.value = true
  try {
    const result = await queryAllCoursesApi()
    if (result.code === 1) {
      courses.value = result.data || []
    } else {
      ElMessage.error(result.msg || '课程列表加载失败')
    }
  } catch (err) {
    ElMessage.error('课程列表加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadCourses)
</script>

<style scoped>
.course-info-page {
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
}
</style>
