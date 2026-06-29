<template>
  <div class="student-home-layout">
    <el-aside class="sidebar" width="200px">
      <div class="avatar-container">
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :http-request="uploadAvatar"
          :before-upload="beforeUpload"
        >
          <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>

      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        router
        background-color="#f5f5f5"
        text-color="#333"
        active-text-color="#b40000"
      >
        <el-sub-menu index="info">
          <template #title>我的信息</template>
          <el-menu-item index="/studenthome/schedule">课程表</el-menu-item>
          <el-menu-item index="/studenthome/baseinfo">基本信息</el-menu-item>
          <el-menu-item index="/studenthome/changepassword">修改密码</el-menu-item>
          <el-menu-item index="/studenthome/academicrecord">学籍档案</el-menu-item>
          <el-menu-item index="/studenthome/newteacher">新生导师</el-menu-item>
          <el-menu-item index="/studenthome/coursefeedback">课程反馈</el-menu-item>
          <el-menu-item index="/studenthome/dualdegree">双专业双学位课程</el-menu-item>
          <el-menu-item index="/studenthome/relatedapply">相片更换申请</el-menu-item>
          <el-menu-item index="/studenthome/learningexp">学习体验</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="service">
          <template #title>公共服务</template>
          <el-menu-item index="/studenthome/program">培养方案</el-menu-item>
          <el-menu-item index="/studenthome/courseinfo">课程信息</el-menu-item>
          <el-menu-item index="/studenthome/timetable">开课安排</el-menu-item>
          <el-menu-item index="/studenthome/studentinfo">学生信息</el-menu-item>
          <el-menu-item index="/studenthome/exam">考试信息</el-menu-item>
          <el-menu-item index="/studenthome/shortmsg">短信平台</el-menu-item>
          <el-menu-item index="/studenthome/classroom">教室资源安排</el-menu-item>
          <el-menu-item index="/studenthome/degreeaudit">双学位课程安排</el-menu-item>
          <el-menu-item index="/studenthome/graduation">毕业生图像采集</el-menu-item>
          <el-menu-item index="/studenthome/makeup">补缓考安排</el-menu-item>
          <el-menu-item index="/studenthome/scorequery">教学查询</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="teaching">
          <template #title>教学信息</template>
          <el-menu-item index="/studenthome/judge">网上评教</el-menu-item>
          <el-menu-item index="/studenthome/questionbox">教务意见箱</el-menu-item>
          <el-menu-item index="/studenthome/examarrange">期末考试安排</el-menu-item>
          <el-menu-item index="/studenthome/doublemajor">辅修双专业双学位报名</el-menu-item>
          <el-menu-item index="/studenthome/outschool">毕业生毕业学位申请</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-main class="main-content">
      <router-view />
      <template v-if="$route.path === '/studenthome'">
        <div class="button-group">
          <el-button type="primary">在读证明</el-button>
          <el-button type="danger">学生父母或监护人信息补录</el-button>
          <el-button type="danger">微信公众号账号绑定</el-button>
        </div>
        <div class="button-group">
          <el-button type="danger">19-20学年第二学期报到注册</el-button>
          <el-button type="primary">江西师范大学Logo水印word模板</el-button>
        </div>
      </template>
    </el-main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getStudentInfoApi, uploadAvatarApi } from '@/api/stu'
import { readLoginUser } from '@/utils/auth'

const avatarUrl = ref('')

onMounted(async () => {
  const user = readLoginUser()
  if (!user) return

  try {
    const result = await getStudentInfoApi()
    if (result.code === 1) {
      avatarUrl.value = result.data?.image || ''
      localStorage.setItem(
        'loginUser',
        JSON.stringify({ ...user, image: result.data?.image || '' })
      )
    } else {
      ElMessage.error('加载学生信息失败')
    }
  } catch (err) {
    ElMessage.error('加载学生信息失败')
  }
})

const uploadAvatar = async ({ file }) => {
  try {
    const result = await uploadAvatarApi(file)
    handleUploadSuccess(result)
  } catch (err) {
    ElMessage.error('上传头像失败')
  }
}

const handleUploadSuccess = (response) => {
  if (response && response.code === 1) {
    avatarUrl.value = response.data
    const user = readLoginUser() || {}
    localStorage.setItem('loginUser', JSON.stringify({ ...user, image: response.data }))
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(response?.msg || '上传头像失败')
  }
}

const beforeUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('上传图片大小不能超过 10MB')
    return false
  }
  return true
}
</script>

<style scoped>
.student-home-layout {
  display: flex;
  min-height: calc(100vh - 200px);
  min-width: 0;
}

.sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #ddd;
  padding-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow-y: auto;
}

.avatar-container {
  margin-bottom: 15px;
  text-align: center;
}

.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #aaa;
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-menu-vertical {
  width: 100%;
  border-right: none;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #fff;
  overflow: auto;
  min-width: 0;
}

@media (max-width: 760px) {
  .student-home-layout { flex-direction: column; }
  .sidebar { width: 100% !important; max-height: 360px; }
}

.button-group {
  margin-top: 20px;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}
</style>
