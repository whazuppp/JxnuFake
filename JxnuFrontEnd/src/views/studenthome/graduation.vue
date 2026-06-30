<script setup>
import { onMounted, reactive } from 'vue'

const student = reactive({
  stuId: '',
  name: '',
  gender: '',
  birthDate: '',
  nation: '',
  idCard: '',
  examNo: '',
  schoolingLength: '',
  institutionCode: '',
  institutionName: '',
  collegeName: '',
  majorName: '',
  className: '',
  educationLevel: '',
  schoolType: '',
  checkedStatus: ''
})

const form = reactive({
  nativePlace: '',
  phone: '',
  remark: ''
})

const formatValue = (value) => value || ''

onMounted(() => {
  const loginUser = localStorage.getItem('loginUser')
  if (!loginUser) return

  try {
    const user = JSON.parse(loginUser)
    student.stuId = user.stuId || user.id || ''
    student.name = user.name || ''
    student.gender = user.gender || ''
    student.birthDate = user.birthDate || ''
    student.nation = user.nation || ''
    student.idCard = user.idCard || ''
    student.examNo = user.examNo || ''
    student.schoolingLength = user.schoolingLength || ''
    student.institutionCode = user.institutionCode || ''
    student.institutionName = user.institutionName || ''
    student.collegeName = user.collegeName || user.college || ''
    student.majorName = user.majorName || user.major || ''
    student.className = user.className || ''
    student.educationLevel = user.educationLevel || ''
    student.schoolType = user.schoolType || ''
    student.checkedStatus = user.checkedStatus || ''
  } catch {
    // Ignore invalid local cache and keep the page structure available.
  }
})
</script>

<template>
  <div class="graduation-page">
    <div class="graduation-card">
      <h2 class="title">江西师范大学毕业生图像采集信息校对表（本部）</h2>
      <p class="status-text">没有找到你的记录！</p>

      <div class="table-scroll">
        <table class="info-table">
          <tbody>
            <tr>
              <th>学号:</th>
              <td>{{ formatValue(student.stuId) }}</td>
              <th>姓名:</th>
              <td>{{ formatValue(student.name) }}</td>
            </tr>
            <tr>
              <th>性别:</th>
              <td>{{ formatValue(student.gender) }}</td>
              <th>民族:</th>
              <td>{{ formatValue(student.nation) }}</td>
            </tr>
            <tr>
              <th>出生日期:</th>
              <td>{{ formatValue(student.birthDate) }}</td>
              <th>身份证号:</th>
              <td>{{ formatValue(student.idCard) }}</td>
            </tr>
            <tr>
              <th>籍贯:</th>
              <td>
                <div class="field-row">
                  <input
                    v-model="form.nativePlace"
                    type="text"
                    class="plain-input native-place-input"
                  />
                  <span class="field-hint">格式：江西省南昌市 或 江西省南昌县</span>
                </div>
              </td>
              <th>手机号:</th>
              <td>
                <input v-model="form.phone" type="text" class="plain-input phone-input" />
              </td>
            </tr>
            <tr>
              <th>考生号:</th>
              <td>{{ formatValue(student.examNo) }}</td>
              <th>培养层次:</th>
              <td>{{ formatValue(student.educationLevel) }}</td>
            </tr>
            <tr>
              <th>学制:</th>
              <td>{{ formatValue(student.schoolingLength) }}</td>
              <th>办学类型:</th>
              <td>{{ formatValue(student.schoolType) }}</td>
            </tr>
            <tr>
              <th>院校代码:</th>
              <td>{{ formatValue(student.institutionCode) }}</td>
              <th>院校名称:</th>
              <td>{{ formatValue(student.institutionName) }}</td>
            </tr>
            <tr>
              <th>学院名称:</th>
              <td>{{ formatValue(student.collegeName) }}</td>
              <th>专业名称:</th>
              <td>{{ formatValue(student.majorName) }}</td>
            </tr>
            <tr>
              <th>班级名称:</th>
              <td>{{ formatValue(student.className) }}</td>
              <th>校验否:</th>
              <td>{{ formatValue(student.checkedStatus) }}</td>
            </tr>
            <tr>
              <th>备注:</th>
              <td colspan="3">{{ formatValue(form.remark) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="action-row">
        <el-button disabled>提交</el-button>
        <el-button disabled>确认不再更改</el-button>
      </div>

      <div class="instruction-block">
        <div class="instruction-title">说明</div>
        <ol class="instruction-list">
          <li>“籍贯”精确到市（县），如江西省南昌市 或江西省南昌县；</li>
          <li>根据新华社要求，毕业生图像采集时需登记手机号，请如实填写，以免影响图像上网；</li>
          <li>以上信息直接反映在你的毕业证书、学籍表上，如有误，请访问此页面区块填写“学籍基本信息修改申请表”交至教学信息科，以免影响毕业作。</li>
        </ol>
      </div>
    </div>
  </div>
</template>

<style scoped>
.graduation-page {
  padding: 8px 0 24px;
  background: #fff;
  color: #222;
}

.graduation-card {
  width: min(100%, 980px);
  margin: 0 auto;
}

.title {
  margin: 0;
  text-align: center;
  font-size: 22px;
  line-height: 1.4;
  font-weight: 700;
  color: #111827;
}

.status-text {
  margin: 10px 0 14px;
  text-align: center;
  font-size: 14px;
  color: #ff4d4f;
}

.table-scroll {
  overflow-x: auto;
}

.info-table {
  width: 100%;
  min-width: 920px;
  border-collapse: collapse;
  table-layout: fixed;
  background: #fff;
}

.info-table th,
.info-table td {
  border: 1px solid #d7d7d7;
  height: 42px;
  padding: 6px 10px;
  font-size: 14px;
  vertical-align: middle;
}

.info-table th {
  width: 18%;
  background: #fff;
  text-align: right;
  font-weight: 700;
  white-space: nowrap;
}

.info-table td {
  width: 32%;
  color: #333;
}

.field-row {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.plain-input {
  height: 28px;
  border: 1px solid #9ca3af;
  border-radius: 2px;
  padding: 0 8px;
  font-size: 14px;
  color: #111827;
  outline: none;
  background: #fff;
}

.plain-input:focus {
  border-color: #6b7280;
}

.native-place-input {
  width: 208px;
}

.phone-input {
  width: 172px;
}

.field-hint {
  color: #666;
  line-height: 1.4;
}

.action-row {
  display: flex;
  justify-content: center;
  gap: 18px;
  padding: 18px 0 10px;
}

.instruction-block {
  margin-top: 6px;
  font-size: 14px;
  color: #222;
}

.instruction-title {
  margin-bottom: 6px;
  font-weight: 700;
}

.instruction-list {
  margin: 0;
  padding-left: 18px;
  line-height: 1.7;
}

@media (max-width: 768px) {
  .graduation-page {
    padding: 0 0 20px;
  }

  .title {
    font-size: 18px;
  }

  .status-text,
  .info-table th,
  .info-table td,
  .instruction-block,
  .plain-input {
    font-size: 13px;
  }

  .action-row {
    flex-wrap: wrap;
  }
}
</style>
