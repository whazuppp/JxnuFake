<script setup>
import { computed } from 'vue'
import { readLoginUser } from '@/utils/auth'

const fallbackStudentNo = '2023262022'

const examBlueprints = [
  {
    courseCode: '262516',
    courseName: 'Web应用技术',
    examTime: '2026-07-06 08:00:00',
    classroomNo: 'X4313g',
    seatNo: '01',
    remark: '专用卡'
  },
  {
    courseCode: '262517',
    courseName: '数据库系统',
    examTime: '2026-07-08 14:00:00',
    classroomNo: 'W2201',
    seatNo: '18',
    remark: '闭卷'
  },
  {
    courseCode: '262518',
    courseName: 'Java程序设计',
    examTime: '2026-07-10 19:00:00',
    classroomNo: 'S301',
    seatNo: '06',
    remark: '上机'
  }
]

const studentNo = computed(() => {
  const loginUser = readLoginUser()
  return loginUser?.studentNo || loginUser?.stuId || loginUser?.studentId || fallbackStudentNo
})

const examRows = computed(() =>
  examBlueprints.map(item => ({
    ...item,
    studentNo: studentNo.value
  }))
)
</script>

<template>
  <section class="exam-page">
    <div class="exam-card">
      <header class="page-header">
        <h2>考试安排</h2>
        <p>2025-2026学年第2学期</p>
      </header>

      <div class="table-wrap">
        <table class="exam-table" aria-label="期末考试安排">
          <thead>
            <tr>
              <th>课程号</th>
              <th>课程名称标识</th>
              <th>学号</th>
              <th>考试时间</th>
              <th>教室号</th>
              <th>座位号</th>
              <th>备注</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in examRows" :key="item.courseCode">
              <td class="code-cell">
                <a href="javascript:void(0)">{{ item.courseCode }}</a>
              </td>
              <td class="course-cell">
                <a href="javascript:void(0)">{{ item.courseName }}</a>
              </td>
              <td>{{ item.studentNo }}</td>
              <td>{{ item.examTime }}</td>
              <td>{{ item.classroomNo }}</td>
              <td>{{ item.seatNo }}</td>
              <td>
                <a href="javascript:void(0)" class="remark-link">{{ item.remark }}</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<style scoped>
.exam-page {
  padding: 8px 0 0;
  background: #fff;
  color: #1f1f1f;
}

.exam-card {
  width: 100%;
}

.page-header {
  margin-bottom: 2px;
  text-align: center;
}

.page-header h2 {
  margin: 0;
  color: #13255c;
  font-size: 28px;
  line-height: 1.2;
  font-family: SimHei, "Microsoft YaHei", sans-serif;
  font-weight: 700;
}

.page-header p {
  margin: 4px 0 0;
  color: #7d7d7d;
  font-size: 12px;
}

.table-wrap {
  overflow-x: auto;
}

.exam-table {
  width: 100%;
  min-width: 960px;
  border-collapse: collapse;
  table-layout: auto;
  font-size: 13px;
}

.exam-table th,
.exam-table td {
  padding: 8px 6px;
  border: 1px solid #f0a468;
  text-align: center;
  white-space: nowrap;
}

.exam-table th {
  color: #fff6cc;
  background: #ad1400;
  font-size: 16px;
  font-family: SimHei, "Microsoft YaHei", sans-serif;
  font-weight: 700;
}

.exam-table td {
  color: #250f98;
  background: #fff;
  font-size: 15px;
  font-family: "Times New Roman", "Microsoft YaHei", serif;
  font-weight: 700;
}

.code-cell,
.course-cell {
  text-align: left;
}

.course-cell {
  min-width: 320px;
}

.exam-table a {
  color: #250f98;
  text-decoration: none;
}

.exam-table a:hover {
  text-decoration: underline;
}

.remark-link {
  display: inline-block;
}

@media (max-width: 768px) {
  .page-header h2 {
    font-size: 24px;
  }

  .exam-table {
    min-width: 820px;
  }

  .exam-table th {
    font-size: 14px;
  }

  .exam-table td {
    font-size: 13px;
  }
}
</style>
