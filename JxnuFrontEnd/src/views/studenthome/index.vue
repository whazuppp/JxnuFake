<template>
  <div class="student-home-layout">
    <el-aside class="sidebar" width="200px">
      <div class="avatar-container" @click="goToRelatedApply">
        <div class="avatar-uploader">
          <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </div>
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
          <el-menu-item index="/studenthome/newteacher">新生导师</el-menu-item>
          <el-menu-item index="/studenthome/dualdegree">双专业双学位课程</el-menu-item>
          <el-menu-item index="/studenthome/relatedapply">相片更换申请</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="service">
          <template #title>公共服务</template>
          <el-menu-item index="/studenthome/program">培养方案</el-menu-item>
          <el-menu-item index="/studenthome/studentinfo">学生信息</el-menu-item>
          <el-menu-item index="/studenthome/teacherinfo">教工信息</el-menu-item>
          <el-menu-item index="/studenthome/graduation">毕业生图像采集</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="teaching">
          <template #title>教学信息</template>
          <el-menu-item index="/studenthome/judge">网上评教</el-menu-item>
          <el-menu-item index="/studenthome/examarrange">期末考试安排</el-menu-item>
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
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getStudentInfoApi } from '@/api/stu'
import { readLoginUser } from '@/utils/auth'
import { STUDENT_AVATAR_UPDATED_EVENT } from '@/utils/avatarApply'

const avatarUrl = ref('')
const router = useRouter()

const syncAvatarFromLocal = () => {
  avatarUrl.value = readLoginUser()?.image || ''
}

onMounted(async () => {
  window.addEventListener(STUDENT_AVATAR_UPDATED_EVENT, syncAvatarFromLocal)
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

onBeforeUnmount(() => {
  window.removeEventListener(STUDENT_AVATAR_UPDATED_EVENT, syncAvatarFromLocal)
})

const goToRelatedApply = () => {
  router.push('/studenthome/relatedapply')
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
  cursor: pointer;
}

.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
  object-fit: cover;
  border: 2px solid #e5e7eb;
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
