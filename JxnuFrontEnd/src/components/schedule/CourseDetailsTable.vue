<template>
  <div class="details-wrap">
    <table class="course-details" aria-label="课程明细">
      <thead>
        <tr>
          <th>课程号</th>
          <th>课程名称标识</th>
          <th>周总课时</th>
          <th>班级名称</th>
          <th>任课老师</th>
          <th>班级同学</th>
          <th>课程讨论区</th>
          <th>评价老师</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="course in courses" :key="course.id">
          <td>{{ course.courseCode }}</td>
          <td class="course-name">{{ course.courseName }}</td>
          <td>{{ course.weeklyPeriods }}</td>
          <td>{{ course.className }}</td>
          <td>{{ course.teacherName }}</td>
          <td><button type="button" @click="$emit('show-roster', course)">查看名单</button></td>
          <td><button type="button" @click="$emit('open-discussion', course)">课程讨论区</button></td>
          <td><button type="button" @click="$emit('open-evaluation', course)">进入</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
defineProps({
  courses: {
    type: Array,
    default: () => []
  }
})

defineEmits(['show-roster', 'open-discussion', 'open-evaluation'])
</script>

<style scoped>
.details-wrap {
  overflow-x: auto;
  border-radius: 0 0 10px 10px;
}

.course-details {
  width: 100%;
  min-width: 920px;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 13px;
}

th,
td {
  border: 1px solid #d7894f;
  padding: 10px 8px;
  text-align: center;
  line-height: 1.5;
}

th {
  background: #9f1d22;
  color: #fff;
  font-weight: 600;
}

td {
  color: #2e4057;
  background: #fff;
}

.course-name {
  width: 210px;
  text-align: left;
  font-weight: 600;
}

button {
  border: 0;
  padding: 0;
  color: #1b5fad;
  background: transparent;
  cursor: pointer;
  text-decoration: underline;
  font: inherit;
}

button:hover {
  color: #b4232a;
}
</style>
