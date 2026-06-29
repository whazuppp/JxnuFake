<template>
  <section class="page">
    <h2>开课安排与选课</h2>
    <el-form :inline="true" :model="filters">
      <el-form-item label="学期"><el-select v-model="filters.semesterId" @change="refresh"><el-option v-for="s in semesters" :key="s.id" :label="s.name" :value="s.id" /></el-select></el-form-item>
      <el-form-item label="课程"><el-input v-model="filters.courseName" clearable /></el-form-item>
      <el-form-item label="教师"><el-input v-model="filters.teacherName" clearable /></el-form-item>
      <el-form-item label="行政班"><el-select v-model="filters.classId" clearable><el-option v-for="c in classes" :key="c.id" :label="c.className" :value="c.id" /></el-select></el-form-item>
      <el-button type="primary" @click="refresh">查询</el-button>
    </el-form>
    <el-table :data="offerings" v-loading="loading" border>
      <el-table-column prop="courseCode" label="课程代码" width="110" />
      <el-table-column prop="courseName" label="课程名称" min-width="160" />
      <el-table-column prop="teacherName" label="教师" width="100" />
      <el-table-column prop="className" label="行政班" min-width="190" />
      <el-table-column label="时间与教室" min-width="220">
        <template #default="{ row }"><div v-for="s in row.schedules" :key="s.id">周{{ s.weekday }} 第{{ s.startPeriod }}-{{ s.endPeriod }}节 · {{ s.building }}{{ s.roomNo }}</div></template>
      </el-table-column>
      <el-table-column label="容量" width="90"><template #default="{ row }">{{ row.studentCount }}/{{ row.capacity }}</template></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="selected.has(row.id)" type="danger" link @click="withdraw(row.id)">退课</el-button>
          <el-button v-else type="primary" link :disabled="row.studentCount >= row.capacity" @click="select(row.id)">选课</el-button>
        </template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryClassesApi, querySemestersApi } from '@/api/reference'
import { queryOfferingsApi } from '@/api/offering'
import { querySelectionsApi, selectOfferingApi, withdrawOfferingApi } from '@/api/studentCourse'
import { businessMessage, selectedOfferingIds, toOfferingParams } from '@/utils/offeringState'

const filters = reactive({ semesterId: null, courseName: '', teacherName: '', classId: null })
const semesters = ref([]), classes = ref([]), offerings = ref([]), selections = ref([]), loading = ref(false)
const selected = computed(() => selectedOfferingIds(selections.value))

async function refresh() {
  if (!filters.semesterId) return
  loading.value = true
  try {
    const [all, mine] = await Promise.all([queryOfferingsApi(toOfferingParams(filters)), querySelectionsApi(filters.semesterId)])
    offerings.value = all.data || []
    selections.value = mine.data || []
  } finally { loading.value = false }
}
async function select(id) {
  const result = await selectOfferingApi(id)
  result.code === 1 ? ElMessage.success('选课成功') : ElMessage.error(businessMessage(result, '选课失败'))
  if (result.code === 1) await refresh()
}
async function withdraw(id) {
  const result = await withdrawOfferingApi(id)
  result.code === 1 ? ElMessage.success('退课成功') : ElMessage.error(businessMessage(result, '退课失败'))
  if (result.code === 1) await refresh()
}
onMounted(async () => {
  const [semesterResult, classResult] = await Promise.all([querySemestersApi(), queryClassesApi()])
  semesters.value = semesterResult.data || []; classes.value = classResult.data || []
  filters.semesterId = semesters.value.find(s => s.current)?.id || semesters.value[0]?.id
  await refresh()
})
</script>

<style scoped>
.page { padding:20px; } h2 { margin-top:0; } .el-select { width:230px; }
</style>
