<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

let router = useRouter();
const loginName = ref("");
const activeImage = ref(0);
const images = [
  new URL("../../assets/img1.jpg", import.meta.url).href,
  new URL("../../assets/img2.jpg", import.meta.url).href,
  new URL("../../assets/img3.jpg", import.meta.url).href
];

onMounted(() => {
  let loginUser = JSON.parse(localStorage.getItem("loginUser"));
  if (loginUser) {
    loginName.value = loginUser.name;
  }
});

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

const handleLogin = () => {
  router.push("/login");
};

const prevImage = () => {
  activeImage.value = (activeImage.value - 1 + images.length) % images.length;
};

const nextImage = () => {
  activeImage.value = (activeImage.value + 1) % images.length;
};
</script>

<template>
  <div class="main-wrapper">
    <div class="content-box">
      <!-- 左侧：图片新闻 -->
      <div class="left-section">
        <div class="image-news">
          <div class="title-bar">图片新闻 <span class="more">更多>></span></div>
          <div class="carousel-container">
            <a :href="`http://localhost:5173/news/notice${activeImage + 1}.html`" target="_blank">
              <img :src="images[activeImage]" class="carousel-image" />
            </a>
            <div class="carousel-controls">
              <button @click="prevImage">《</button>
              <button @click="nextImage">》</button>
            </div>
          </div>
        </div>
        <div class="related-links-box">
          <div class="title-bar">相关链接</div>
          <ul class="related-links">
            <li><a href="https://jwc.nenu.edu.cn/" target="_blank">东北师范大学教务处</a></li>
            <li><a href="https://jwc.sicnu.edu.cn/" target="_blank">四川师范大学教务处</a></li>
            <li><a href="https://jwb.bnu.edu.cn/" target="_blank">北京师范大学教务部</a></li>
          </ul>
        </div>
      </div>

      <!-- 中间：教务通知 + 教务公告 -->
      <div class="middle-section">
        <div class="news-list">
          <div class="title-bar">教务通知 <span class="more">更多>></span></div>
          <ul>
            <li v-for="i in 4" :key="i">
              <a :href="`/news/notice${i}.html`" target="_blank">
                教务字（2026）5{{ i }}号 关于...
              </a>
            </li>
          </ul>
        </div>

        <div class="news-list">
          <div class="title-bar">教务公告 <span class="more">更多>></span></div>
          <ul>
            <li v-for="i in 3" :key="'g'+i">
              <a :href="`/news/announcement${i}.html`" target="_blank">
                教务公告（2026）第{{ i }}号...
              </a>
            </li>
          </ul>
        </div>
      </div>

      <!-- 右侧：登录 + 服务入口 -->
      <div class="right-section">
        <div class="login-box">
          <el-card class="login-card" shadow="hover">
            <div class="login-title">登录窗口</div>
            <template v-if="!loginName">
              <el-button type="primary" class="login-btn" @click="handleLogin">
                1.统一身份认证
              </el-button>
              <el-button type="primary" class="login-btn" disabled>
                2.学号登录
              </el-button>
              <el-button type="primary" class="login-btn" disabled>
                3.职工账号登录
              </el-button>
              <el-button type="primary" class="login-btn" disabled>
                4.学生家长登录
              </el-button>
            </template>
            <template v-else>
              <p class="welcome-text">欢迎您，{{ loginName }}</p>
              <el-button type="primary" icon="el-icon-switch-button" @click="logout">注销</el-button>
            </template>
          </el-card>

          <!-- 服务链接区 -->
          <div class="service-box">
            <div class="service-title">服务导航</div>
            <ul class="service-list">
              <li><a href="https://vgms.fanyu.com/" target="_blank">毕业论文管理</a></li>
              <li><a href="https://www.icourse163.org/" target="_blank">国家慕课平台</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>


    <footer class="footer">
      <p class="company-name">2026web应用技术大作业</p>
    </footer>
  </div>
</template>

<style scoped>
.main-wrapper {
  padding: 0 40px;
  background-color: #f9f9f9;
}

.content-box {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.left-section, .middle-section, .right-section {
  background-color: white;
  padding: 20px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.left-section {
  flex: 1.2;
}

.middle-section {
  flex: 1.5;
}

.right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.title-bar {
  font-weight: bold;
  margin-bottom: 10px;
  border-bottom: 2px solid #ccc;
  padding-bottom: 5px;
}

.more {
  float: right;
  font-weight: normal;
  font-size: 12px;
  cursor: pointer;
  color: #888;
}

.carousel-container {
  position: relative;
}

.carousel-image {
  width: 100%;
  height: auto;
}

.carousel-controls {
  text-align: center;
  margin-top: 10px;
}



.news-list ul {
  list-style: none;
  padding: 0;
}

.news-list li {
  margin-bottom: 8px;
}

.login-card {
  padding: 20px;
  background-color: #f4faff;
}

.login-title {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 16px;
}

.login-btn {
  display: block;
  width: 100%;
  margin-bottom: 10px;
}

.welcome-text {
  margin-bottom: 12px;
  font-size: 16px;
  color: #007bff;
}

.service-box {
  margin-top: 20px;
}

.service-title {
  font-weight: bold;
  margin-bottom: 10px;
}

.service-list {
  list-style: none;
  padding-left: 0;
}

.service-list li {
  margin-bottom: 8px;
}

.related-links {
  list-style: none;
  padding-left: 0;
}

.related-links li {
  margin-bottom: 6px;
}


.banner-placeholder {
  height: 120px;
  background-color: #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.footer {
  margin-top: 30px;
  background-color: #c2c0c0;
  color: white;
  text-align: center;
  padding: 10px 0;
}
</style>
