# 考试安排页面 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将学生之家“期末考试安排”页面实现为高还原老教务风格页面，并展示 2025-2026 学年第2学期的真实课程名与前端维护的考试安排字段。

**Architecture:** 仅修改前端页面与原型测试文件，不扩展后端接口。页面内部维护考试安排原型数据，课程号与课程名固定对应仓库第二学期种子中的三门真实课程，学号从本地登录信息读取并兜底。

**Tech Stack:** Vue 3 `script setup`、Vitest、现有 `readLoginUser` 工具函数

---

### Task 1: 锁定页面行为

**Files:**
- Modify: `D:\JxnuProject\JxnuFrontEnd\src\views\prototype-pages.test.js`
- Test: `D:\JxnuProject\JxnuFrontEnd\src\views\prototype-pages.test.js`

- [ ] **Step 1: Write the failing test**

```js
it('期末考试安排页面展示第二学期真实课程与原型表头', () => {
  const examArrange = readFileSync(new URL('./studenthome/examarrange.vue', import.meta.url), 'utf8')

  expect(examArrange).toContain('考试安排')
  expect(examArrange).toContain('课程号')
  expect(examArrange).toContain('课程名称标识')
  expect(examArrange).toContain('考试时间')
  expect(examArrange).toContain('262516')
  expect(examArrange).toContain('Web应用技术')
  expect(examArrange).toContain('262517')
  expect(examArrange).toContain('数据库系统')
  expect(examArrange).toContain('262518')
  expect(examArrange).toContain('Java程序设计')
})
```

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: FAIL because `examarrange.vue` is still empty.

- [ ] **Step 3: Write minimal implementation**

```vue
<script setup>
import { computed } from 'vue'
import { readLoginUser } from '@/utils/auth'

const fallbackStudentNo = '2023262022'
const examRows = [
  { courseCode: '262516', courseName: 'Web应用技术' },
  { courseCode: '262517', courseName: '数据库系统' },
  { courseCode: '262518', courseName: 'Java程序设计' }
]

const studentNo = computed(() => readLoginUser()?.studentNo || readLoginUser()?.stuId || fallbackStudentNo)
</script>
```

- [ ] **Step 4: Run test to verify it passes**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add D:/JxnuProject/JxnuFrontEnd/src/views/prototype-pages.test.js D:/JxnuProject/JxnuFrontEnd/src/views/studenthome/examarrange.vue D:/JxnuProject/docs/superpowers/plans/2026-06-30-exam-arrangement-page.md
git commit -m "feat: add exam arrangement prototype page"
```

### Task 2: 还原老教务视觉细节

**Files:**
- Modify: `D:\JxnuProject\JxnuFrontEnd\src\views\studenthome\examarrange.vue`
- Test: `D:\JxnuProject\JxnuFrontEnd\src\views\prototype-pages.test.js`

- [ ] **Step 1: Write the failing test**

```js
expect(examArrange).toContain('remark-link')
expect(examArrange).toContain('exam-table')
expect(examArrange).toContain('课程名称标识')
```

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: FAIL because the style markers are not implemented yet.

- [ ] **Step 3: Write minimal implementation**

```vue
<table class="exam-table">
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
</table>
```

- [ ] **Step 4: Run test to verify it passes**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add D:/JxnuProject/JxnuFrontEnd/src/views/studenthome/examarrange.vue D:/JxnuProject/JxnuFrontEnd/src/views/prototype-pages.test.js
git commit -m "style: restore classic exam arrangement page"
```
