<template>
  <section class="roster-page">
    <div class="roster-actions">
      <el-button type="primary" plain data-test="back" @click="$emit('back')">
        返回课程表
      </el-button>
    </div>

    <h2>江西师范大学学生花名册</h2>
    <div class="course-meta">
      <span>课程名称：<strong>{{ offering?.courseName || '-' }}</strong></span>
      <span>班级名称：<strong>{{ offering?.className || '-' }}</strong></span>
      <span>开课学期：<strong>{{ offering?.semesterName || '-' }}</strong></span>
      <span>任课教师：<strong>{{ offering?.teacherName || '-' }}</strong></span>
    </div>

    <div v-loading="loading" class="roster-content">
      <div v-if="cards.length" class="roster-grid">
        <article v-for="student in cards" :key="student.studentNo" class="student-card">
          <div class="serial">序号：{{ student.index }}</div>
          <img
            :src="student.image"
            :alt="`${student.name}的头像`"
            @error="useDefaultAvatar"
          />
          <dl>
            <div><dt>学号：</dt><dd>{{ student.studentNo }}</dd></div>
            <div><dt>姓名：</dt><dd>{{ student.name }}</dd></div>
            <div><dt>班级：</dt><dd>{{ student.className }}</dd></div>
          </dl>
        </article>
      </div>
      <el-empty v-else-if="!loading" description="该开课班暂无学生" />
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { DEFAULT_AVATAR, toRosterCards } from '@/utils/schedulePresentation'

const props = defineProps({
  offering: {
    type: Object,
    default: null
  },
  students: {
    type: Array,
    default: () => []
  },
  loading: Boolean
})

defineEmits(['back'])

const cards = computed(() => toRosterCards(props.students))

function useDefaultAvatar(event) {
  if (event.target.src !== DEFAULT_AVATAR) event.target.src = DEFAULT_AVATAR
}
</script>

<style scoped>
.roster-page {
  position: relative;
  min-height: 520px;
  padding: 8px 0 32px;
}

.roster-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 4px;
}

h2 {
  margin: 0 0 8px;
  text-align: center;
  color: #182432;
  font-family: "Microsoft YaHei", sans-serif;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px 28px;
  margin-bottom: 14px;
  color: #26384b;
}

.course-meta strong {
  border-bottom: 1px solid #7b8793;
  font-weight: 500;
}

.roster-content {
  min-height: 360px;
}

.roster-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  border-top: 1px solid #dc8c52;
  border-left: 1px solid #dc8c52;
}

.student-card {
  min-width: 0;
  padding: 12px 10px 14px;
  border-right: 1px solid #dc8c52;
  border-bottom: 1px solid #dc8c52;
  background: #fff;
}

.serial {
  margin-bottom: 8px;
  color: #252525;
}

img {
  display: block;
  width: min(162px, 88%);
  aspect-ratio: 3 / 4;
  margin: 0 auto 10px;
  object-fit: cover;
  background: #e8eef4;
}

dl,
dd {
  margin: 0;
}

dl > div {
  display: grid;
  grid-template-columns: 48px minmax(0, 1fr);
  margin-top: 6px;
  line-height: 1.5;
}

dt {
  font-weight: 600;
}

dd {
  overflow-wrap: anywhere;
}

@media (max-width: 1050px) {
  .roster-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .roster-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 480px) {
  .roster-grid {
    grid-template-columns: 1fr;
  }
}
</style>
