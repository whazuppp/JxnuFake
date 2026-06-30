<template>
  <section class="judge-page">
    <template v-if="viewMode === 'list'">
      <header class="judge-header">
        <div class="picker-row">
          <div class="picker-label">请选择评价学期:</div>
          <el-select
            v-model="pendingSemesterId"
            class="semester-select"
            placeholder="请选择学期"
          >
            <el-option
              v-for="item in semesters"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-button
            type="danger"
            :loading="loading"
            :disabled="!pendingSemesterId"
            @click="applySemesterSelection"
          >
            确定
          </el-button>
        </div>
        <div class="semester-meta">
          当前系统设置的评教学期：{{ activeSemester?.startDate || '-' }}
        </div>
      </header>

      <div v-loading="loading" class="judge-table-wrap">
        <table v-if="courseRows.length" class="judge-table" aria-label="网上评教课程列表">
          <thead>
            <tr>
              <th>课程性质</th>
              <th>课程号</th>
              <th>课程名称标识</th>
              <th>任课教师</th>
              <th>评分</th>
              <th>评语</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="course in courseRows" :key="course.id">
              <td>
                <span class="course-type" :class="courseTypeClass(course.courseType)">
                  {{ course.courseType || '未分类' }}
                </span>
              </td>
              <td>{{ course.courseCode || '-' }}</td>
              <td class="course-name">
                {{ course.courseName || '-' }}
                <span v-if="course.credit" class="course-credit">（{{ course.credit }}分）</span>
              </td>
              <td>{{ course.teacherName || '-' }}</td>
              <td>{{ evaluationStatus(course.id) }}</td>
              <td>
                <button type="button" class="link-button" @click="openEvaluation(course)">
                  {{ evaluationRecords[course.id] ? '查看' : '进入' }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <el-empty v-else-if="!loading" description="该学期暂无可展示的评教课程" />
      </div>
    </template>

    <template v-else>
      <header class="detail-title">教师语义评价窗口</header>

      <section class="detail-panel">
        <div class="detail-section-heading">教师语义评价</div>

        <div class="detail-grid">
          <div class="detail-row">
            <span class="detail-label">课程号：</span>
            <span>{{ activeOffering?.courseCode || '-' }}</span>
            <span class="detail-label">开课学期：</span>
            <span>{{ activeOffering?.semesterName || activeSemester?.name || '-' }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">课程名称：</span>
            <span>{{ activeOffering?.courseName || '-' }}</span>
            <span class="detail-label">任课老师：</span>
            <span>{{ activeOffering?.teacherName || '-' }}</span>
          </div>
          <div class="detail-row textarea-row">
            <label class="detail-label" for="judge-comment">我的评论：</label>
            <div class="comment-area">
              <textarea
                id="judge-comment"
                v-model="commentDraft"
                rows="6"
                placeholder="请输入不少于30个字的匿名评语"
              />
              <div class="comment-note">内容不少于30个字。</div>
            </div>
          </div>
        </div>

        <div class="detail-actions">
          <el-button type="danger" @click="submitEvaluation">提交</el-button>
          <button type="button" class="secondary-button" @click="backToList">返回课程列表</button>
          <span class="anonymous-note">（评价内容将以匿名的方式呈现给任课老师）</span>
        </div>
      </section>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { querySemestersApi } from '@/api/reference'
import { queryTimetableApi } from '@/api/studentCourse'
import { normalizeCourseRows } from '@/utils/timetable'

const route = useRoute()
const semesters = ref([])
const pendingSemesterId = ref()
const activeSemesterId = ref()
const timetable = ref(null)
const loading = ref(false)
const viewMode = ref('list')
const activeOffering = ref(null)
const commentDraft = ref('')
const evaluationRecords = ref({})

const activeSemester = computed(() =>
  semesters.value.find(item => Number(item.id) === Number(activeSemesterId.value))
)
const courseRows = computed(() => normalizeCourseRows(timetable.value?.courses || []))

function normalizeId(value) {
  const parsed = Number(value)
  return Number.isFinite(parsed) && parsed > 0 ? parsed : undefined
}

function courseTypeClass(courseType = '') {
  if (courseType.includes('公共')) return 'type-public'
  if (courseType.includes('任选')) return 'type-elective'
  if (courseType.includes('专业')) return 'type-major'
  return 'type-default'
}

function evaluationStatus(offeringId) {
  return evaluationRecords.value[offeringId] ? '已评价' : '未评价'
}

function openEvaluation(offering) {
  activeOffering.value = offering
  commentDraft.value = evaluationRecords.value[offering.id]?.comment || ''
  viewMode.value = 'evaluation'
}

function openEvaluationById(offeringId) {
  const target = courseRows.value.find(item => Number(item.id) === Number(offeringId))
  if (!target) return false
  openEvaluation(target)
  return true
}

function backToList() {
  viewMode.value = 'list'
  activeOffering.value = null
  commentDraft.value = ''
}

async function loadTimetable(semesterId) {
  loading.value = true
  try {
    const result = await queryTimetableApi(semesterId)
    if (result.code !== 1) throw new Error(result.msg || '评教课程加载失败')
    timetable.value = result.data
    return true
  } catch (error) {
    timetable.value = null
    ElMessage.error(error.message || '评教课程加载失败')
    return false
  } finally {
    loading.value = false
  }
}

async function applySemesterSelection() {
  const nextSemesterId = normalizeId(pendingSemesterId.value)
  if (!nextSemesterId) return

  activeSemesterId.value = nextSemesterId
  backToList()

  const loaded = await loadTimetable(nextSemesterId)
  if (!loaded) return

  const requestedOfferingId = normalizeId(route.query.offeringId)
  if (requestedOfferingId) openEvaluationById(requestedOfferingId)
}

async function initializeJudgePage() {
  try {
    const result = await querySemestersApi()
    if (result.code !== 1) throw new Error(result.msg || '学期加载失败')

    semesters.value = result.data || []

    const requestedSemesterId = normalizeId(route.query.semesterId)
    const defaultSemesterId =
      requestedSemesterId ||
      semesters.value.find(item => item.current)?.id ||
      semesters.value[0]?.id

    pendingSemesterId.value = defaultSemesterId
    await applySemesterSelection()
  } catch (error) {
    ElMessage.error(error.message || '学期加载失败')
  }
}

function submitEvaluation() {
  const content = commentDraft.value.trim()
  if (content.length < 30) {
    ElMessage.warning('评语内容不能少于30个字')
    return
  }

  const offeringId = activeOffering.value?.id
  if (!offeringId) return

  evaluationRecords.value = {
    ...evaluationRecords.value,
    [offeringId]: {
      comment: content
    }
  }

  ElMessage.success('教师语义评价已提交')
  backToList()
}

watch(
  () => [route.query.offeringId, route.query.semesterId],
  async ([offeringId, semesterId], [previousOfferingId, previousSemesterId]) => {
    if (!semesters.value.length) return

    const nextSemesterId = normalizeId(semesterId) || activeSemesterId.value
    if (normalizeId(nextSemesterId) && Number(nextSemesterId) !== Number(activeSemesterId.value)) {
      pendingSemesterId.value = nextSemesterId
      await applySemesterSelection()
      return
    }

    if (offeringId && (offeringId !== previousOfferingId || semesterId !== previousSemesterId)) {
      openEvaluationById(offeringId)
    }
  }
)

onMounted(() => {
  initializeJudgePage()
})
</script>

<style scoped>
.judge-page {
  min-height: 620px;
  padding: 28px 24px;
  color: #243447;
  background:
    linear-gradient(180deg, rgba(224, 232, 238, 0.55), rgba(255, 255, 255, 0) 120px),
    #fff;
}

.judge-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  margin-bottom: 18px;
}

.picker-row {
  display: flex;
  align-items: center;
  gap: 0;
}

.picker-label {
  padding: 10px 18px;
  color: #fff;
  background: #ef3f3f;
  font-size: 16px;
  font-weight: 600;
}

.semester-select {
  width: 440px;
}

.semester-select :deep(.el-input__wrapper) {
  min-height: 42px;
  border-radius: 0;
  box-shadow: inset 0 0 0 1px #ef3f3f;
}

.picker-row :deep(.el-button) {
  min-height: 42px;
  border-radius: 0 4px 4px 0;
}

.semester-meta {
  color: #4e5967;
  font-size: 15px;
}

.judge-table-wrap {
  min-height: 420px;
  border: 1px solid #e0e4e9;
  background: #fff;
}

.judge-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.judge-table th,
.judge-table td {
  border: 1px solid #e0e4e9;
  padding: 11px 10px;
  text-align: center;
  font-size: 15px;
}

.judge-table th {
  color: #243447;
  background: #fafbfd;
  font-weight: 700;
}

.course-name {
  color: #334155;
}

.course-credit {
  color: #64748b;
}

.course-type {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 70px;
  padding: 3px 8px;
  border-radius: 4px;
  color: #fff;
  font-size: 13px;
}

.type-public {
  background: #ef4444;
}

.type-major {
  background: #14b8a6;
}

.type-elective {
  background: #22c55e;
}

.type-default {
  background: #64748b;
}

.link-button,
.secondary-button {
  border: 0;
  color: #1f4f8c;
  background: transparent;
  cursor: pointer;
  font: inherit;
}

.link-button:hover,
.secondary-button:hover {
  color: #d23a3a;
}

.detail-title {
  margin-bottom: 18px;
  text-align: center;
  font-size: 28px;
  font-weight: 500;
}

.detail-panel {
  border: 1px solid #d5dde6;
  border-radius: 6px;
  overflow: hidden;
  background: #fff;
}

.detail-section-heading {
  padding: 16px 22px;
  border-bottom: 1px solid #d5dde6;
  background: #f8fafc;
  font-size: 18px;
  font-weight: 700;
}

.detail-grid {
  display: flex;
  flex-direction: column;
}

.detail-row {
  display: grid;
  grid-template-columns: 140px 1fr 140px 1fr;
  gap: 10px;
  padding: 16px 14px;
  border-bottom: 1px solid #e5ebf1;
  align-items: center;
  font-size: 16px;
}

.detail-label {
  color: #0f172a;
}

.textarea-row {
  align-items: flex-start;
}

.comment-area {
  max-width: 600px;
}

.comment-area textarea {
  width: 100%;
  min-height: 150px;
  padding: 12px;
  border: 1px solid #9ca3af;
  border-radius: 4px;
  resize: vertical;
  font: inherit;
}

.comment-note {
  margin-top: 6px;
}

.detail-actions {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 22px;
}

.anonymous-note {
  color: #334155;
  font-size: 15px;
}

@media (max-width: 900px) {
  .judge-page {
    padding: 18px 12px;
  }

  .judge-header,
  .picker-row,
  .detail-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .semester-select {
    width: 100%;
  }

  .detail-row {
    grid-template-columns: 1fr;
  }
}
</style>
