# 学期课表与选课范围调整 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 增加未来学期课表与选课，并按原型恢复 2025-2026 学年第1学期固定课表。

**Architecture:** 前端学期工具统一识别目标学期，并提供历史固定课表数据；课程表按学期决定使用固定数据或后端数据；选课页只暴露未来学期。SQL 种子为未来学期复制待选开课班和排课，但不创建学生选课关系。

**Tech Stack:** Vue 3、Element Plus、Vitest、MySQL

---

### Task 1: 学期规则与历史固定课表

**Files:**
- Create: `JxnuFrontEnd/src/utils/semesterSchedule.js`
- Create: `JxnuFrontEnd/src/utils/semesterSchedule.test.js`

- [ ] **Step 1: Write the failing test**

测试 `selectNextSemester()` 只返回 `2026-2027/1`，并断言 `historicalFirstTermCourses` 包含原型中的 11 门课程、课程号及关键排课。

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/utils/semesterSchedule.test.js`
Expected: FAIL，因为模块尚不存在。

- [ ] **Step 3: Write minimal implementation**

导出：

```js
export const NEXT_ACADEMIC_YEAR = '2026-2027'
export const NEXT_TERM_NO = 1
export const isSemester = (semester, year, termNo) =>
  semester?.academicYear === year && Number(semester?.termNo) === termNo
export const selectNextSemester = semesters =>
  semesters.filter(item => isSemester(item, NEXT_ACADEMIC_YEAR, NEXT_TERM_NO))
```

`historicalFirstTermCourses` 写入课程号
`002174、004040、251176、262021、262092、262275、262276、262366、262367、262371、262410`
及原型对应的课程名称、教师、班级、周课时和排课对象。

- [ ] **Step 4: Run test to verify it passes**

Run: `npm test -- src/utils/semesterSchedule.test.js`
Expected: PASS。

### Task 2: 接入课程表和选课页面

**Files:**
- Modify: `JxnuFrontEnd/src/views/studenthome/schedule.vue`
- Modify: `JxnuFrontEnd/src/views/studycenter/course-select.vue`
- Test: `JxnuFrontEnd/src/utils/semesterSchedule.test.js`

- [ ] **Step 1: Write the failing integration assertions**

读取两个 Vue 文件，断言课程表使用 `coursesForSemester()`，选课页使用 `selectNextSemester()`，并且选课学期下拉不可切换。

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/utils/semesterSchedule.test.js`
Expected: FAIL，因为页面尚未接入规则。

- [ ] **Step 3: Write minimal implementation**

课程表在 `2025-2026/1` 时使用 `historicalFirstTermCourses`，其余学期使用接口数据；选课页将接口学期列表过滤为 `2026-2027/1`，并禁用单一学期下拉。

- [ ] **Step 4: Run test to verify it passes**

Run: `npm test -- src/utils/semesterSchedule.test.js`
Expected: PASS。

### Task 3: 未来学期开课数据

**Files:**
- Create: `sql/06-seed-2026-first-semester.sql`
- Modify: `sql/02-seed-reference-data.sql`
- Modify: `JxnuFrontEnd/src/utils/sqlSeed.test.js`

- [ ] **Step 1: Write the failing SQL assertions**

断言种子包含 `2026-2027/1`，从 2025-2026 开课班复制未来开课班与排课，且不向未来学期插入 `student_course`。

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/utils/sqlSeed.test.js`
Expected: FAIL，因为未来学期种子尚不存在。

- [ ] **Step 3: Write minimal SQL**

新增目标学期；按课程、教师、班级复制历史开课班到目标学期；按新旧开课班映射复制排课；不创建学生选课关系。同步基础初始化脚本中的学期与未来开课数据。

- [ ] **Step 4: Run test to verify it passes**

Run: `npm test -- src/utils/sqlSeed.test.js`
Expected: PASS。

### Task 4: 完整验证

- [ ] **Step 1: Run focused tests**

Run: `npm test -- src/utils/semesterSchedule.test.js src/utils/sqlSeed.test.js`
Expected: 全部 PASS。

- [ ] **Step 2: Run all frontend tests**

Run: `npm test`
Expected: 全部 PASS。

- [ ] **Step 3: Build frontend**

Run: `npm run build`
Expected: Exit code 0。
