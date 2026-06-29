<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

let router = useRouter();
const loginName = ref("");

// 登录状态检测
onMounted(() => {
  let loginUser = JSON.parse(localStorage.getItem("loginUser"));
  if (loginUser) {
    loginName.value = loginUser.name;
  }
});

// 注销功能
const logout = () => {
  ElMessageBox.confirm("确认退出登录吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    ElMessage.success("退出登录成功");
    localStorage.removeItem("loginUser");
    router.push("/login");
  });
};
</script>

<template>
  <div class="common-layout">
    <!-- 顶部标题 -->
    <div class="top-banner">
      <img src="../../assets/toppic.png" alt="banner" class="banner-img" />
      <div class="banner-text">系统培养，个性服务</div>
    </div>

    <!-- 顶部菜单栏 -->
    <el-header class="top-nav">
      <el-menu
        mode="horizontal"
        router
        :default-active="$route.path"
        class="top-menu"
        background-color="#b40000"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/index">首页</el-menu-item>
        <el-menu-item index="/studenthome">学生之家</el-menu-item>
        <el-menu-item index="/teacherhome">教工之家</el-menu-item>
        <el-menu-item index="/office">网上办公</el-menu-item>

        <el-sub-menu index="/teaching-org">
          <template #title>教学组织</template>
          <el-menu-item index="/teaching-org/org-functions">机构职能</el-menu-item>
          <el-menu-item index="/teaching-org/monitoring">常态监控</el-menu-item>
          <el-menu-item index="/teaching-org/major-assess">专业评估</el-menu-item>
          <el-menu-item index="/teaching-org/project-review">项目评审</el-menu-item>
          <el-menu-item index="/teaching-org/audit">审核评估</el-menu-item>
          <el-menu-item index="/teaching-org/data-collect">状态数据采集v3.0</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/teaching-rules">教学制度</el-menu-item>

        <el-sub-menu index="/teaching-resource">
          <template #title>教学资源</template>
          <el-menu-item index="/teaching-resource/online-platform">网络教学平台</el-menu-item>
          <el-menu-item index="/teaching-resource/micro-course">正大微课</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/interaction">
          <template #title>教学互动</template>
          <el-menu-item index="/interaction/course-discussion">课程讨论</el-menu-item>
          <el-menu-item index="/interaction/stuopinion">建议意见</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/studycenter">
          <template #title>学习中心</template>
          <el-menu-item index="/studycenter/course-select">选课</el-menu-item>
          <el-menu-item index="/studycenter/evaluation">评教</el-menu-item>
          <el-menu-item index="/studycenter/nav">入学导航</el-menu-item>
          <el-menu-item index="/studycenter/tutoring">辅导</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>


    <!-- 主展示区域 -->
    <el-main>
      <router-view />
    </el-main>
  </div>
</template>

<style scoped>
.common-layout {
  font-family: "Microsoft Yahei", sans-serif;
  position: relative;
}

.top-banner {
  position: relative;
  width: 100%;
  height: 150px;
  overflow: hidden;
}

.banner-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-text {
  position: absolute;
  bottom: 10px;
  right: 20px;
  color: #b40000;
  font-size: 24px;
  font-family: "楷体";
  font-weight: bold;
}

.top-nav {
  background-color: #b40000;
  overflow-x: auto;
}

.top-menu {
  display: flex;
  flex-wrap: nowrap;
  flex: 1;
  border-bottom: none;
}



.el-menu-item,
.el-sub-menu__title {
  font-size: 16px;
  padding: 0 20px;
  line-height: 60px;
}

.el-menu-item:hover,
.el-sub-menu__title:hover {
  background-color: #a00000 !important;
}

.welcome-text {
  color: red;
  font-size: 16px;
  margin-bottom: 12px;
}
</style>
