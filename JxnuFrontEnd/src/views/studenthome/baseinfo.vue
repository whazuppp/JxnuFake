<template>
  <div class="baseinfo-container">
    <h2 class="title">江西师范大学学生信息核对表</h2>

    <table class="info-table">
      <tbody>
        <tr>
          <td>学号</td>
          <td>{{ student.stuId }}</td>
          <td>姓名</td>
          <td>{{ student.name }}</td>
        </tr>
        <tr>
          <td>班级</td>
          <td colspan="3">23级计算机科学与技术2班</td>
        </tr>
        <tr>
          <td>相片 image</td>
          <td colspan="3">
            <el-image
              style="width: 120px; height: 160px"
              :src="student.image"
              fit="cover"
              :preview-src-list="[student.image]"
            >
              <template #error>
                <div style="font-size: 12px; color: #999">若未找到照片，请与教务信息科联系</div>
              </template>
            </el-image>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="reminder">
      以下信息为您在教育部
      <a href="https://www.chsi.com.cn" target="_blank">中国高等教育学生信息网</a>
      上的信息，信息直接反映在您以后毕业证书及电子注册信息上，请您认真核对。
      若有信息错误，请填写在下方栏：下载信息修改申请表
    </div>

    <table class="info-table">
      <tbody>
        <tr>
          <td>姓名</td>
          <td>{{ student.name }}</td>
          <td>班级</td>
          <td colspan="3">23级计算机科学与技术2班</td>
        </tr>
        <tr>
          <td>性别</td>
          <td>{{ '男' }}</td>
          <td>入学时间</td>
          <td>2023-09-01</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const student = ref({})

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('loginUser'))
  if (user) {
    student.value = user
  }
})
</script>

<style scoped>
.baseinfo-container {
  padding: 20px;
}
.title {
  text-align: center;
  margin-bottom: 20px;
}
.info-table {
  width: 90%;
  margin: 20px auto;
  border-collapse: collapse;
  font-size: 14px;
}
.info-table td {
  border: 1px solid #ccc;
  padding: 10px;
  vertical-align: middle;
}
.reminder {
  font-size: 14px;
  margin: 20px auto;
  max-width: 600px;
  color: #555;
  line-height: 1.6;
}
.reminder a {
  color: #0077cc;
  text-decoration: underline;
}
</style>
