<template>
  <section class="course-select-page">
    <header class="page-header">
      <div>
        <h2>学生网上选课</h2>
        <p>可切换第一学期或第二学期，查看对应学期的可选开课班并完成选课、退课。</p>
      </div>
      <el-button type="primary" plain :loading="loading" @click="loadRows">刷新</el-button>
    </header>

    <el-card class="filter-card" shadow="never">
      <el-form :inline="true" :model="filters" label-width="72px">
        <el-form-item label="学期">
          <el-select
            v-model="filters.semesterId"
            placeholder="请选择学期"
            class="semester-select"
            @change="loadRows"
          >
            <el-option
              v-for="semester in semesters"
              :key="semester.id"
              :label="semester.name"
              :value="semester.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input
            v-model.trim="filters.courseName"
            clearable
            placeholder="请输入课程名称"
            @keyup.enter="loadRows"
            @clear="loadRows"
          />
        </el-form-item>
        <el-form-item label="教师">
          <el-input
            v-model.trim="filters.teacherName"
            clearable
            placeholder="请输入教师姓名"
            @keyup.enter="loadRows"
            @clear="loadRows"
          />
        </el-form-item>
        <el-form-item label="班级">
          <el-select
            v-model="filters.classId"
            clearable
            filterable
            placeholder="全部班级"
            class="class-select"
            @change="loadRows"
            @clear="loadRows"
          >
            <el-option
              v-for="clazz in classes"
              :key="clazz.id"
              :label="clazz.className"
              :value="clazz.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="loadRows">查询</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="summary-bar">
      <span>当前学期：<strong>{{ currentSemesterName }}</strong></span>
      <span>可选课程：<strong>{{ rows.length }}</strong> 门</span>
      <span>已选课程：<strong>{{ selectedCount }}</strong> 门</span>
    </div>

    <el-table
      v-loading="loading"
      :data="rows"
      border
      stripe
      row-key="id"
      empty-text="当前学期暂无可选课程"
      class="selection-table"
    >
      <el-table-column prop="courseCode" label="课程号" width="110" />
      <el-table-column prop="courseName" label="课程名称" min-width="190" show-overflow-tooltip />
      <el-table-column label="类型" width="90">
        <template #default="{ row }">{{ courseTypeText(row.courseType) }}</template>
      </el-table-column>
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column prop="weeklyPeriods" label="周课时" width="90" />
      <el-table-column prop="teacherName" label="任课教师" width="110" />
      <el-table-column prop="className" label="开课班级" min-width="170" show-overflow-tooltip />
      <el-table-column label="上课时间地点" min-width="260">
        <template #default="{ row }">{{ scheduleText(row.schedules) }}</template>
      </el-table-column>
      <el-table-column label="容量" width="110">
        <template #default="{ row }">{{ row.studentCount || 0 }}/{{ row.capacity }}</template>
      </el-table-column>
      <el-table-column label="状态" width="95">
        <template #default="{ row }">
          <el-tag v-if="row.selected" type="success">已选</el-tag>
          <el-tag v-else-if="row.remaining <= 0" type="danger">已满</el-tag>
          <el-tag v-else type="info">可选</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="110">
        <template #default="{ row }">
          <el-button
            v-if="row.selected"
            link
            type="danger"
            :loading="operatingId === row.id"
            @click="withdraw(row)"
          >
            退课
          </el-button>
          <el-button
            v-else
            link
            type="primary"
            :disabled="row.remaining <= 0"
            :loading="operatingId === row.id"
            @click="select(row)"
          >
            选课
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryOfferingsApi } from '@/api/offering'
import { queryClassesApi, querySemestersApi } from '@/api/reference'
import {
  querySelectionsApi,
  selectOfferingApi,
  withdrawOfferingApi
} from '@/api/studentCourse'
import { businessMessage, toOfferingParams, toSelectionRows } from '@/utils/offeringState'

const weekdays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']

const semesters = ref([])
const classes = ref([])
const offerings = ref([])
const selections = ref([])
const loading = ref(false)
const operatingId = ref(null)

const filters = reactive({
  semesterId: undefined,
  courseName: '',
  teacherName: '',
  classId: null
})

const rows = computed(() => toSelectionRows(offerings.value, selections.value))
const selectedCount = computed(() => rows.value.filter(row => row.selected).length)
const currentSemesterName = computed(() => {
  return semesters.value.find(item => item.id === filters.semesterId)?.name || '-'
})

function courseTypeText(type) {
  const map = {
    THEORY: '理论',
    EXPERIMENT: '实验',
    PRACTICE: '实践'
  }
  return map[type] || type || '-'
}

function scheduleText(schedules = []) {
  if (!schedules.length) return '未排课'
  return schedules.map(schedule => {
    const day = weekdays[schedule.weekday - 1] || `星期${schedule.weekday}`
    const room = `${schedule.building || ''}${schedule.roomNo || ''}` || '教室待定'
    return `${day} 第${schedule.startPeriod}-${schedule.endPeriod}节 ${room}`
  }).join('；')
}

async function loadOptions() {
  const [semesterResult, classResult] = await Promise.all([
    querySemestersApi(),
    queryClassesApi()
  ])
  if (semesterResult.code !== 1) throw new Error(semesterResult.msg || '学期加载失败')
  if (classResult.code !== 1) throw new Error(classResult.msg || '班级加载失败')
  semesters.value = semesterResult.data || []
  classes.value = classResult.data || []
  filters.semesterId = semesters.value.find(item => item.current)?.id || semesters.value[0]?.id
}

async function loadRows() {
  if (!filters.semesterId) {
    offerings.value = []
    selections.value = []
    return
  }

  loading.value = true
  try {
    const params = toOfferingParams(filters)
    const [offeringResult, selectionResult] = await Promise.all([
      queryOfferingsApi(params),
      querySelectionsApi(filters.semesterId)
    ])
    if (offeringResult.code !== 1) throw new Error(offeringResult.msg || '开课班加载失败')
    if (selectionResult.code !== 1) throw new Error(selectionResult.msg || '已选课程加载失败')
    offerings.value = offeringResult.data || []
    selections.value = selectionResult.data || []
  } catch (error) {
    offerings.value = []
    selections.value = []
    ElMessage.error(error.message || '选课数据加载失败')
  } finally {
    loading.value = false
  }
}

function resetFilters() {
  filters.courseName = ''
  filters.teacherName = ''
  filters.classId = null
  loadRows()
}

async function select(row) {
  operatingId.value = row.id
  try {
    const result = await selectOfferingApi(row.id)
    if (result.code !== 1) throw result
    ElMessage.success('选课成功')
    await loadRows()
  } catch (error) {
    ElMessage.error(businessMessage(error, '选课失败'))
  } finally {
    operatingId.value = null
  }
}

async function withdraw(row) {
  operatingId.value = row.id
  try {
    const result = await withdrawOfferingApi(row.id)
    if (result.code !== 1) throw result
    ElMessage.success('退课成功')
    await loadRows()
  } catch (error) {
    ElMessage.error(businessMessage(error, '退课失败'))
  } finally {
    operatingId.value = null
  }
}

onMounted(async () => {
  try {
    await loadOptions()
    await loadRows()
  } catch (error) {
    ElMessage.error(error.message || '选课页面初始化失败')
  }
})
</script>

<style scoped>
.course-select-page {
  min-height: 600px;
  padding: 24px;
  color: #253242;
  background: #fff;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 16px;
}

h2 {
  margin: 0 0 6px;
  color: #172838;
  font-size: 24px;
}

.page-header p {
  margin: 0;
  color: #667789;
  font-size: 13px;
}

.filter-card {
  margin-bottom: 12px;
}

.semester-select {
  width: 240px;
}

.class-select {
  width: 220px;
}

.summary-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 32px;
  margin-bottom: 12px;
  padding: 10px 14px;
  border: 1px solid #dfe6ec;
  border-radius: 8px;
  background: #f7fafc;
  color: #44566c;
  font-size: 13px;
}

.summary-bar strong {
  color: #b4232a;
  font-weight: 600;
}

.selection-table {
  width: 100%;
}

@media (max-width: 760px) {
  .course-select-page {
    padding: 16px 10px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .semester-select,
  .class-select {
    width: min(240px, 72vw);
  }
}
</style>
