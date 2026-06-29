<template>
  <section class="page">
    <header>
      <div>
        <h2>我的课程表</h2>
        <p v-if="timetable">{{ timetable.studentName }} · {{ timetable.className }}</p>
      </div>
      <el-select v-model="semesterId" placeholder="选择学期" @change="loadTimetable">
        <el-option v-for="item in semesters" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </header>

    <div v-loading="loading" class="table-wrap">
      <table v-if="timetable?.courses?.length" class="schedule-table">
        <thead><tr><th>节次</th><th v-for="day in weekdays" :key="day">{{ day }}</th></tr></thead>
        <tbody>
          <tr v-for="(row, rowIndex) in grid" :key="rowIndex">
            <th>第 {{ rowIndex + 1 }} 节</th>
            <td v-for="cell in row" v-show="!cell.hidden" :key="cell.weekday"
                :rowspan="cell.rowspan" :class="{ course: cell.offering }">
              <button v-if="cell.offering" @click="showRoster(cell.offering)">
                <strong>{{ cell.offering.courseName }}</strong>
                <span>{{ cell.offering.teacherName }}</span>
                <span>{{ room(cell.offering.activeSchedule) }}</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <el-empty v-else-if="!loading" description="本学期暂无已选课程" />
    </div>

    <el-dialog v-model="rosterVisible" :title="`${activeCourse?.courseName || ''} · 学生名单`" width="620px">
      <el-table :data="students" v-loading="rosterLoading">
        <el-table-column prop="studentNo" label="学号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="className" label="行政班" min-width="190" />
      </el-table>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { querySemestersApi } from '@/api/reference'
import { queryTimetableApi } from '@/api/studentCourse'
import { queryOfferingStudentsApi } from '@/api/offering'
import { buildTimetableGrid } from '@/utils/timetable'

const weekdays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']
const semesters = ref([])
const semesterId = ref()
const timetable = ref(null)
const loading = ref(false)
const rosterVisible = ref(false)
const rosterLoading = ref(false)
const students = ref([])
const activeCourse = ref(null)
const grid = computed(() => buildTimetableGrid(timetable.value?.courses || []))
const room = schedule => `${schedule?.building || ''}${schedule?.roomNo || ''}`

async function loadTimetable() {
  if (!semesterId.value) return
  loading.value = true
  try {
    const result = await queryTimetableApi(semesterId.value)
    if (result.code !== 1) throw new Error(result.msg)
    timetable.value = result.data
  } catch (error) {
    ElMessage.error(error.message || '课程表加载失败')
  } finally {
    loading.value = false
  }
}

async function showRoster(offering) {
  activeCourse.value = offering
  rosterVisible.value = true
  rosterLoading.value = true
  try {
    const result = await queryOfferingStudentsApi(offering.id)
    students.value = result.code === 1 ? result.data || [] : []
    if (result.code !== 1) ElMessage.error(result.msg)
  } finally {
    rosterLoading.value = false
  }
}

onMounted(async () => {
  const result = await querySemestersApi()
  semesters.value = result.data || []
  semesterId.value = semesters.value.find(item => item.current)?.id || semesters.value[0]?.id
  await loadTimetable()
})
</script>

<style scoped>
.page { padding: 20px; } header { display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; }
h2 { margin:0 0 6px; } p { margin:0; color:#777; } .table-wrap { overflow:auto; min-height:260px; }
.schedule-table { width:100%; min-width:900px; border-collapse:collapse; table-layout:fixed; }
th, td { border:1px solid #dcdfe6; height:58px; text-align:center; } th { background:#f5f7fa; }
th:first-child { width:76px; } td.course { background:#ecf5ff; padding:0; }
button { width:100%; height:100%; border:0; background:transparent; color:#305680; cursor:pointer; display:flex; flex-direction:column; justify-content:center; gap:4px; }
</style>
