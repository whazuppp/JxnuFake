<template>
  <section class="course-select-page">
    <!-- 选课系统入口页 -->
    <div v-if="!entered" class="gateway-page">
      <div class="brand-bar">
        <div class="brand-logo">JXNU</div>
        <h1>江西师范大学&nbsp;&nbsp;选课系统</h1>
      </div>

      <div class="gateway-card">
      <div class="open-time-box">
      <p>开放时间：{{ todayStartTime }} 至 {{ todayEndTime }}</p>
      <p>{{ systemStatus }}</p>
      </div>

        <div class="login-box">
          <el-button type="warning" size="large" class="login-btn" @click="enterSelection">
            统一身份认证系统登录
          </el-button>

          <div class="helper-links">
            <a href="javascript:void(0)" @click="showConfig">查看配置</a>
            <a href="javascript:void(0)" @click="checkTime">检查时间</a>
          </div>
        </div>
      </div>
    </div>

    <!-- 登录后进入真正选课页 -->
    <div v-else class="selection-page">
      <header class="page-header">
        <div>
          <h2>学生选课</h2>
        </div>

        <div class="header-actions">
          <el-button plain @click="entered = false">返回选课入口</el-button>

          <el-select
            v-model="semesterId"
            placeholder="请选择学期"
            class="semester-select"
            disabled
            @change="loadData"
          >
            <el-option
              v-for="item in semesters"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </div>
      </header>

      <div class="status-bar">
        <span>当前学期：{{ currentSemesterName }}</span>
        <span>开课班数量：{{ offerings.length }}</span>
        <span>已选课程：{{ selectedIds.size }}</span>
      </div>

      <el-table
        v-loading="loading"
        :data="offerings"
        border
        stripe
        empty-text="该学期暂无可选课程"
      >
        <el-table-column prop="courseCode" label="课程号" width="110" />
        <el-table-column prop="courseName" label="课程名称" min-width="190" />
        <el-table-column label="课程类型" width="110">
          <template #default="{ row }">
            {{ courseTypeName(row.courseType) }}
          </template>
        </el-table-column>
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="weeklyPeriods" label="周学时" width="90" />
        <el-table-column prop="teacherName" label="任课教师" width="110" />
        <el-table-column prop="className" label="开课班级" min-width="170" />
        <el-table-column label="上课时间地点" min-width="260">
          <template #default="{ row }">
            {{ formatSchedules(row.schedules) }}
          </template>
        </el-table-column>
        <el-table-column label="容量" width="110">
          <template #default="{ row }">
            {{ row.studentCount || 0 }}/{{ row.capacity }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="selectedIds.has(row.id)" type="success">已选</el-tag>
            <el-tag v-else-if="isFull(row)" type="danger">已满</el-tag>
            <el-tag v-else type="info">未选</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="!selectedIds.has(row.id)"
              type="primary"
              link
              :disabled="isFull(row)"
              @click="selectCourse(row.id)"
            >
              选课
            </el-button>

            <el-button
              v-else
              type="danger"
              link
              @click="withdrawCourse(row.id)"
            >
              退课
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { querySemestersApi } from '@/api/reference'
import { queryOfferingsApi } from '@/api/offering'
import {
  querySelectionsApi,
  selectOfferingApi,
  withdrawOfferingApi
} from '@/api/studentCourse'
import { businessMessage, selectedOfferingIds } from '@/utils/offeringState'
import { selectNextSemester } from '@/utils/semesterSchedule'

const entered = ref(false)
const semesters = ref([])
const semesterId = ref()
const offerings = ref([])
const selections = ref([])
const loading = ref(false)

const selectedIds = computed(() => selectedOfferingIds(selections.value))

const currentSemesterName = computed(() => {
  return semesters.value.find(item => item.id === semesterId.value)?.name || '-'
})

async function enterSelection() {
  entered.value = true
  if (semesterId.value) {
    await loadData()
  }
}

async function loadSemesters() {
  const result = await querySemestersApi()
  if (result.code !== 1) {
    throw new Error(result.msg || '学期加载失败')
  }

  semesters.value = selectNextSemester(result.data || [])
  semesterId.value = semesters.value[0]?.id

  if (!semesterId.value) {
    throw new Error('未找到2026-2027学年第1学期')
  }

  if (semesterId.value) {
    await loadData()
  }
}

async function loadData() {
  if (!semesterId.value) return

  loading.value = true
  try {
    const [offeringsResult, selectionsResult] = await Promise.all([
      queryOfferingsApi({ semesterId: semesterId.value }),
      querySelectionsApi(semesterId.value)
    ])

    if (offeringsResult.code !== 1) {
      throw new Error(offeringsResult.msg || '开课班加载失败')
    }
    if (selectionsResult.code !== 1) {
      throw new Error(selectionsResult.msg || '已选课程加载失败')
    }

    offerings.value = offeringsResult.data || []
    selections.value = selectionsResult.data || []
  } catch (error) {
    offerings.value = []
    selections.value = []
    ElMessage.error(error.message || '选课页面加载失败')
  } finally {
    loading.value = false
  }
}

async function selectCourse(id) {
  try {
    const result = await selectOfferingApi(id)
    if (result.code !== 1) {
      throw new Error(businessMessage(result, '选课失败'))
    }

    ElMessage.success('选课成功')
    await loadData()
  } catch (error) {
    ElMessage.error(error.message || '选课失败')
  }
}

async function withdrawCourse(id) {
  try {
    const result = await withdrawOfferingApi(id)
    if (result.code !== 1) {
      throw new Error(businessMessage(result, '退课失败'))
    }

    ElMessage.success('退课成功')
    await loadData()
  } catch (error) {
    ElMessage.error(error.message || '退课失败')
  }
}

function isFull(row) {
  return Number(row.studentCount || 0) >= Number(row.capacity || 0)
}

function courseTypeName(type) {
  const map = {
    THEORY: '理论',
    EXPERIMENT: '实验',
    PRACTICE: '实践'
  }
  return map[type] || type || '-'
}

const todayStartTime = computed(() => `${formatToday()} 00:00:00`)
const todayEndTime = computed(() => `${formatToday()} 24:00:00`)

const systemStatus = computed(() => {
  return '系统开放中'
})

function formatToday() {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function formatSchedules(schedules = []) {
  if (!schedules.length) return '暂未排课'

  const weekdayNames = {
    1: '周一',
    2: '周二',
    3: '周三',
    4: '周四',
    5: '周五',
    6: '周六',
    7: '周日'
  }

  return schedules
    .map(item => {
      const day = weekdayNames[item.weekday] || `周${item.weekday}`
      const room = `${item.building || ''}${item.roomNo || ''}` || '教室待定'
      return `${day} 第${item.startPeriod}-${item.endPeriod}节 ${room}`
    })
    .join('；')
}

function showConfig() {
  ElMessage.info('当前为选课系统演示配置')
}

function checkTime() {
  ElMessage.info('当前时间检查通过，可继续进入选课页面')
}



onMounted(async () => {
  try {
    await loadSemesters()
  } catch (error) {
    ElMessage.error(error.message || '学期加载失败')
  }
})
</script>

<style scoped>
.course-select-page {
  min-height: 640px;
  color: #253242;
}

.gateway-page {
  position: relative;
  min-height: 640px;
  padding: 24px;
  overflow: hidden;
  background:
    linear-gradient(rgba(250, 250, 235, 0.58), rgba(250, 250, 235, 0.58)),
    url('@/assets/login-bg.png') center / cover no-repeat;
}

.gateway-page::after {
  position: absolute;
  inset: 0;
  content: '';
  pointer-events: none;
  backdrop-filter: blur(1px);
}

.brand-bar {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 18px;
  color: #067346;
  font-family: "Microsoft YaHei", sans-serif;
}

.brand-logo {
  display: flex;
  width: 54px;
  height: 54px;
  align-items: center;
  justify-content: center;
  border: 3px solid #087348;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.74);
  color: #087348;
  font-size: 13px;
  font-weight: 700;
}

.brand-bar h1 {
  margin: 0;
  font-size: 34px;
  font-weight: 700;
  letter-spacing: 4px;
}

.gateway-card {
  position: relative;
  z-index: 1;
  width: min(586px, calc(100vw - 56px));
  margin: 150px auto 0;
  padding: 28px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 8px 28px rgba(40, 62, 80, 0.16);
}

.open-time-box {
  padding: 14px 18px;
  border: 1px solid #dea2c4;
  border-radius: 5px;
  background: #e6b3cf;
  color: #8a1f59;
  text-align: center;
  font-size: 18px;
  line-height: 1.55;
}

.open-time-box p {
  margin: 0;
}

.login-box {
  margin-top: 26px;
  padding: 30px 20px;
  border: 1px solid #dcdfe6;
  border-radius: 5px;
  text-align: center;
}

.login-btn {
  padding: 11px 22px;
  font-size: 18px;
  font-weight: 700;
}

.helper-links {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 24px;
}

.helper-links a {
  color: #e95f2c;
  font-size: 16px;
  text-decoration: none;
}

.helper-links a:hover {
  text-decoration: underline;
}

.selection-page {
  min-height: 640px;
  padding: 24px;
  background: #fff;
}

.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
  padding-bottom: 18px;
  border-bottom: 1px solid #dfe6ec;
}

.page-header h2 {
  margin: 0;
  color: #172838;
  font-size: 24px;
}

.page-header p {
  margin: 8px 0 0;
  color: #6b7785;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.semester-select {
  width: 250px;
}

.status-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 32px;
  margin: 16px 0;
  padding: 12px 16px;
  border: 1px solid #d8e5ef;
  border-radius: 6px;
  background: #f6fbff;
  color: #39556e;
}

@media (max-width: 760px) {
  .brand-bar h1 {
    font-size: 24px;
  }

  .gateway-card {
    margin-top: 90px;
  }

  .page-header {
    align-items: stretch;
    flex-direction: column;
  }

  .header-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .semester-select {
    width: 100%;
  }
}
</style>
