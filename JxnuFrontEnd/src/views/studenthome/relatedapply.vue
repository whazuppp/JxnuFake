<template>
  <div class="related-apply-page">
    <h2 class="page-title">学生相片更换申请</h2>

    <section class="notice-card">
      <p class="student-line">
        班级：{{ profile.className }}
        学号：{{ profile.studentNo }}
        姓名：{{ profile.studentName }}
      </p>
      <p class="notice-text">
        【说明】：1、每位在校学生只能申请2次更换照片；2、毕业班学生统一使用电子图像采集的照片，不接受在线更换。
        3、仅接受证件照及类似性质的照片，不接受美颜或非本人的照片。
      </p>
    </section>

    <section class="form-card">
      <h3 class="section-title">提交申请</h3>
      <el-form @submit.prevent>
        <el-form-item label="相片：">
          <div class="upload-row">
            <input ref="fileInputRef" type="file" accept=".jpg,.jpeg" @change="handleFileChange" />
            <el-button type="primary" @click="submitApply">提交</el-button>
          </div>
          <p class="upload-tip">请选择 jpg 格式的相片</p>
        </el-form-item>
      </el-form>
    </section>

    <section class="table-card">
      <table class="records-table">
        <thead>
          <tr>
            <th>编号</th>
            <th>提交时间</th>
            <th>相片</th>
            <th>状态</th>
            <th>审核时间</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="records.length === 0">
            <td colspan="7" class="empty-cell">暂无申请记录</td>
          </tr>
          <tr v-for="record in records" :key="record.id">
            <td>{{ record.id }}</td>
            <td>{{ record.submittedAt }}</td>
            <td>
              <img class="record-image" :src="record.imageUrl" alt="申请相片" />
            </td>
            <td>{{ record.status }}</td>
            <td>{{ record.reviewedAt }}</td>
            <td>{{ record.remark || '-' }}</td>
            <td>
              <el-button type="danger" link @click="removeRecord(record.id)">删除</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getStudentInfoApi, uploadAvatarApi } from '@/api/stu'
import { readLoginUser } from '@/utils/auth'
import {
  approveAvatarApplyRecord,
  createAvatarApplyRecord,
  readAvatarApplyRecords,
  resolveStudentProfile,
  saveAvatarApplyRecords,
  STUDENT_AVATAR_UPDATED_EVENT,
  validateAvatarFile
} from '@/utils/avatarApply'

const student = ref({})
const records = ref([])
const selectedFile = ref(null)
const fileInputRef = ref(null)
const approvalTimers = new Map()

const profile = computed(() => resolveStudentProfile(student.value))

onMounted(async () => {
  const localUser = readLoginUser()
  if (localUser) {
    student.value = localUser
  }
  records.value = readAvatarApplyRecords().sort((a, b) => b.id - a.id)

  try {
    const result = await getStudentInfoApi()
    if (result.code === 1 && result.data) {
      const mergedUser = { ...(readLoginUser() || {}), ...result.data }
      student.value = mergedUser
      localStorage.setItem('loginUser', JSON.stringify(mergedUser))
    }
  } catch {
    // 保留本地信息回显
  }
})

const markApprovedLater = (recordId, imageUrl) => {
  const timerId = window.setTimeout(() => {
    const latestRecords = readAvatarApplyRecords()
    const approvedRecords = approveAvatarApplyRecord(latestRecords, recordId)
    records.value = approvedRecords.sort((a, b) => b.id - a.id)
    saveAvatarApplyRecords(approvedRecords)

    const mergedUser = { ...(readLoginUser() || {}), image: imageUrl }
    student.value = { ...student.value, image: imageUrl }
    localStorage.setItem('loginUser', JSON.stringify(mergedUser))
    window.dispatchEvent(new Event(STUDENT_AVATAR_UPDATED_EVENT))
    approvalTimers.delete(recordId)
  }, 5000)

  approvalTimers.set(recordId, timerId)
}

const handleFileChange = (event) => {
  const [file] = event.target.files || []
  if (!file) {
    selectedFile.value = null
    return
  }

  const validation = validateAvatarFile(file)
  if (!validation.valid) {
    selectedFile.value = null
    event.target.value = ''
    ElMessage.error(validation.message)
    return
  }

  selectedFile.value = file
}

const submitApply = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择符合要求的 JPG 相片')
    return
  }

  try {
    const result = await uploadAvatarApi(selectedFile.value)
    if (result.code !== 1) {
      ElMessage.error(result.msg || '提交申请失败')
      return
    }

    const imageUrl = result.data
    const nextRecord = createAvatarApplyRecord(imageUrl)
    const nextRecords = [nextRecord, ...records.value]
    records.value = nextRecords
    saveAvatarApplyRecords(nextRecords)
    markApprovedLater(nextRecord.id, imageUrl)

    selectedFile.value = null
    if (fileInputRef.value) {
      fileInputRef.value.value = ''
    }
    ElMessage.success('相片更换申请已提交')
  } catch {
    ElMessage.error('提交申请失败')
  }
}

const removeRecord = (recordId) => {
  const timerId = approvalTimers.get(recordId)
  if (timerId) {
    window.clearTimeout(timerId)
    approvalTimers.delete(recordId)
  }

  const nextRecords = records.value.filter((record) => record.id !== recordId)
  records.value = nextRecords
  saveAvatarApplyRecords(nextRecords)
  ElMessage.success('提交记录已删除')
}
</script>

<style scoped>
.related-apply-page {
  color: #222;
}

.page-title {
  margin: 0 0 24px;
  text-align: center;
  font-size: 30px;
  font-weight: 500;
}

.notice-card,
.form-card,
.table-card {
  border: 1px solid #d9d9d9;
  background: #fff;
  margin-bottom: 24px;
}

.notice-card {
  border-color: #ef4444;
  border-left-width: 8px;
  padding: 28px 40px;
}

.student-line {
  margin: 0 0 24px;
  text-align: center;
  font-size: 18px;
}

.notice-text {
  margin: 0;
  text-align: center;
  line-height: 1.8;
  font-size: 16px;
}

.form-card {
  padding: 18px 24px 8px;
}

.section-title {
  margin: 0 0 12px;
  text-align: center;
  font-size: 18px;
  font-weight: 500;
}

.upload-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.upload-tip {
  margin: 10px 0 0;
  color: #555;
}

.records-table {
  width: 100%;
  border-collapse: collapse;
}

.records-table th,
.records-table td {
  border: 1px solid #d9d9d9;
  padding: 12px;
  text-align: left;
  vertical-align: top;
}

.records-table th {
  background: #fafafa;
  font-size: 16px;
}

.record-image {
  width: 180px;
  max-width: 100%;
  height: auto;
  display: block;
}

.empty-cell {
  text-align: center;
  color: #888;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 24px;
  }

  .notice-card {
    padding: 20px 16px;
  }

  .student-line,
  .notice-text {
    text-align: left;
  }

  .records-table {
    display: block;
    overflow-x: auto;
  }
}
</style>
