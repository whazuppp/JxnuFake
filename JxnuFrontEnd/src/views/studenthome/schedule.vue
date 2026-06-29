<template>
  <section class="schedule-page">
    <CourseRoster
      v-if="viewMode === 'roster'"
      :offering="activeCourse"
      :students="students"
      :loading="rosterLoading"
      @back="backToSchedule"
    />

    <template v-else>
      <header class="page-header">
        <div>
          <p class="eyebrow">STUDENT TIMETABLE</p>
          <h2>江西师范大学学生课程表</h2>
        </div>
        <label class="semester-picker">
          <span>选择学期</span>
          <el-select
            v-model="semesterId"
            placeholder="请选择学期"
            @change="handleSemesterChange"
          >
            <el-option
              v-for="item in semesters"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </label>
      </header>

      <div class="student-strip">
        <span>班级名称：<strong>{{ timetable?.className || '-' }}</strong></span>
        <span>学号：<strong>{{ timetable?.studentNo || '-' }}</strong></span>
        <span>姓名：<strong>{{ timetable?.studentName || '-' }}</strong></span>
      </div>

      <div v-loading="loading" class="schedule-content">
        <div v-if="courseRows.length" class="schedule-wrap">
          <table class="schedule-table" aria-label="学生课程表">
            <thead>
              <tr>
                <th class="period-column">节次</th>
                <th v-for="day in weekdays" :key="day">{{ day }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, rowIndex) in grid" :key="rowIndex">
                <th class="period-column">第 {{ rowIndex + 1 }} 节</th>
                <td
                  v-for="cell in row"
                  v-show="!cell.hidden"
                  :key="cell.weekday"
                  :rowspan="cell.rowspan"
                  :class="{ 'has-course': cell.offering }"
                >
                  <div v-if="cell.offering" class="course-block">
                    <strong>{{ cell.offering.courseName }}</strong>
                    <span>{{ room(cell.offering.activeSchedule) }}</span>
                    <span>{{ cell.offering.className }}</span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="schedule-note">
            共 {{ courseRows.length }} 门课程 · 点击下方“查看名单”进入班级花名册
          </div>

          <CourseDetailsTable
            :courses="courseRows"
            @show-roster="showRoster"
            @open-discussion="openDiscussion"
            @open-evaluation="openEvaluation"
          />
        </div>
        <el-empty v-else-if="!loading" description="该学期暂无课程安排" />
      </div>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import CourseDetailsTable from '@/components/schedule/CourseDetailsTable.vue'
import CourseRoster from '@/components/schedule/CourseRoster.vue'
import { querySemestersApi } from '@/api/reference'
import { queryTimetableApi } from '@/api/studentCourse'
import { queryOfferingStudentsApi } from '@/api/offering'
import { buildTimetableGrid, normalizeCourseRows } from '@/utils/timetable'

const router = useRouter()
const weekdays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']
const semesters = ref([])
const semesterId = ref()
const timetable = ref(null)
const loading = ref(false)
const viewMode = ref('schedule')
const rosterLoading = ref(false)
const students = ref([])
const activeCourse = ref(null)

const courses = computed(() => timetable.value?.courses || [])
const courseRows = computed(() => normalizeCourseRows(courses.value))
const grid = computed(() => buildTimetableGrid(courses.value))

function room(schedule) {
  if (!schedule) return '教室待定'
  return `${schedule.building || ''}${schedule.roomNo || ''}` || '教室待定'
}

async function loadTimetable() {
  if (!semesterId.value) return
  loading.value = true
  try {
    const result = await queryTimetableApi(semesterId.value)
    if (result.code !== 1) throw new Error(result.msg || '课程表加载失败')
    timetable.value = result.data
  } catch (error) {
    timetable.value = null
    ElMessage.error(error.message || '课程表加载失败')
  } finally {
    loading.value = false
  }
}

async function handleSemesterChange() {
  backToSchedule()
  await loadTimetable()
}

async function showRoster(offering) {
  activeCourse.value = offering
  students.value = []
  viewMode.value = 'roster'
  rosterLoading.value = true
  try {
    const result = await queryOfferingStudentsApi(offering.id)
    if (result.code !== 1) throw new Error(result.msg || '班级名单加载失败')
    students.value = result.data || []
  } catch (error) {
    ElMessage.error(error.message || '班级名单加载失败')
  } finally {
    rosterLoading.value = false
  }
}

function backToSchedule() {
  viewMode.value = 'schedule'
  activeCourse.value = null
  students.value = []
}

function openDiscussion(offering) {
  router.push({ path: '/interaction/course-discussion', query: { offeringId: offering.id } })
}

function openEvaluation(offering) {
  router.push({ path: '/studycenter/evaluation', query: { offeringId: offering.id } })
}

onMounted(async () => {
  try {
    const result = await querySemestersApi()
    if (result.code !== 1) throw new Error(result.msg || '学期加载失败')
    semesters.value = result.data || []
    semesterId.value = semesters.value.find(item => item.current)?.id || semesters.value[0]?.id
    await loadTimetable()
  } catch (error) {
    ElMessage.error(error.message || '学期加载失败')
  }
})
</script>

<style scoped>
.schedule-page {
  min-height: 600px;
  padding: 24px;
  color: #253242;
  background:
    radial-gradient(circle at 100% 0, rgba(42, 132, 175, 0.08), transparent 260px),
    #fff;
}

.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 24px;
  padding-bottom: 18px;
  border-bottom: 1px solid #dfe6ec;
}

.eyebrow {
  margin: 0 0 4px;
  color: #1e799a;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.15em;
}

h2 {
  margin: 0;
  color: #172838;
  font-size: 24px;
}

.semester-picker {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #526273;
  font-size: 13px;
}

.semester-picker :deep(.el-select) {
  width: 240px;
}

.student-strip {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 12px 48px;
  padding: 14px 12px;
  color: #394c5f;
}

.student-strip strong {
  color: #172838;
  border-bottom: 1px solid #8392a2;
  font-weight: 500;
}

.schedule-content {
  min-height: 360px;
}

.schedule-wrap {
  overflow-x: auto;
}

.schedule-table {
  width: 100%;
  min-width: 940px;
  border-collapse: collapse;
  table-layout: fixed;
  border: 1px solid #168da9;
}

.schedule-table th,
.schedule-table td {
  height: 54px;
  border: 1px solid #aebcc8;
  text-align: center;
  vertical-align: middle;
}

.schedule-table thead th {
  height: 38px;
  color: #064c63;
  background: #35c8dc;
  font-size: 13px;
}

.period-column {
  width: 72px;
  color: #40576c;
  background: #edf2f5;
  font-size: 12px;
}

td.has-course {
  padding: 0;
  background: #d9fff1;
}

.course-block {
  display: flex;
  min-height: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 3px;
  padding: 7px 5px;
  color: #075b70;
  line-height: 1.35;
}

.course-block strong {
  font-size: 13px;
}

.course-block span {
  font-size: 11px;
}

.schedule-note {
  padding: 8px 12px;
  border: 1px solid #168da9;
  border-top: 0;
  color: #8d2025;
  background: #d9f8fb;
  text-align: center;
  font-size: 12px;
}

@media (max-width: 700px) {
  .schedule-page {
    padding: 16px 10px;
  }

  .page-header {
    align-items: stretch;
    flex-direction: column;
  }

  .semester-picker {
    justify-content: space-between;
  }

  .semester-picker :deep(.el-select) {
    width: min(240px, 70vw);
  }
}
</style>
