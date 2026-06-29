# 学生课程表重设计实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 让学生按学期查看完整课程表、课程明细，并在主界面查看带头像的班级花名册。

**Architecture:** 沿用现有 `student_course → course_offering → course_schedule` 数据流；后端仅扩展名单头像字段，前端将课表和花名册拆成同页两种视图。SQL 种子脚本补齐两个学期的课程、排课和选课关系，不引入新表。

**Tech Stack:** Vue 3、Element Plus、Vitest、Spring Boot、MyBatis、JUnit 5、Mockito、MySQL 8。

---

### Task 1: 后端名单头像

**Files:**
- Modify: `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/vo/StudentSummaryVO.java`
- Modify: `JxnuBackEnd/src/main/resources/mapper/OfferingMapper.xml`
- Modify: `JxnuBackEnd/src/test/java/com/mygroup5people/jxnufake/service/impl/OfferingServiceImplTest.java`

- [ ] **Step 1: 写失败测试**

在 `OfferingServiceImplTest` 中构造包含头像地址的 `StudentSummaryVO`，调用 `listStudents` 后断言返回对象保留该地址：

```java
StudentSummaryVO student = new StudentSummaryVO();
student.setImage("https://example.com/avatar.jpg");
when(offeringMapper.listStudents(10)).thenReturn(List.of(student));
assertEquals("https://example.com/avatar.jpg",
        service.listStudents(10, 1).get(0).getImage());
```

- [ ] **Step 2: 验证测试失败**

Run: `.\mvnw.cmd -Dtest=OfferingServiceImplTest test`

Expected: 编译失败，提示 `StudentSummaryVO` 不存在 `setImage/getImage`。

- [ ] **Step 3: 最小实现**

在 `StudentSummaryVO` 增加：

```java
private String image;
```

名单 SQL 增加：

```sql
s.image
```

- [ ] **Step 4: 验证测试通过**

Run: `.\mvnw.cmd -Dtest=OfferingServiceImplTest test`

Expected: `BUILD SUCCESS`。

### Task 2: 两个学期的种子数据

**Files:**
- Modify: `sql/02-seed-reference-data.sql`
- Modify: `sql/03-seed-enrollments.sql`

- [ ] **Step 1: 写种子数据静态验收测试**

新增前端 Vitest 脚本读取 SQL 并断言：

```js
expect(seed).toContain("'002926', '西汉海昏侯墓与海昏侯刘贺研究'")
expect(seed).toContain("'DL2025', '深度学习'")
expect(seed).toContain("'CPP2025', 'C++设计'")
expect(seed).toContain("'COMP2025', '编译原理'")
expect(seed).toContain("'WEB2025', 'Web程序设计'")
```

并断言选课脚本不再只限定 `262516`。

- [ ] **Step 2: 验证测试失败**

Run: `npm test -- sqlSeed.test.js`

Expected: 缺少原型课程和历史学期课程。

- [ ] **Step 3: 实现可重复执行的 SQL**

`02-seed-reference-data.sql` 使用 `INSERT ... ON DUPLICATE KEY UPDATE` 补齐：

- 本学期 8 门课程、教师、教室、开课班和排课。
- 上学期 4 门课程，周课时依次为 3、4、4、4，并补齐开课班和排课。

`03-seed-enrollments.sql` 将现有学生加入两个学期的全部种子开课班：

```sql
INSERT INTO student_course (...)
SELECT ...
FROM student s
CROSS JOIN course_offering o
JOIN semester sem ON sem.id = o.semester_id
WHERE sem.academic_year = '2025-2026'
  AND NOT EXISTS (...);
```

- [ ] **Step 4: 验证静态测试通过**

Run: `npm test -- sqlSeed.test.js`

Expected: 测试通过。

### Task 3: 课表展示模型

**Files:**
- Modify: `JxnuFrontEnd/src/utils/timetable.js`
- Modify: `JxnuFrontEnd/src/utils/timetable.test.js`

- [ ] **Step 1: 写失败测试**

增加课程明细归一化测试：

```js
expect(normalizeCourseRows([{ id: 1, courseCode: '262516', weeklyPeriods: 3 }]))
  .toEqual([{ id: 1, courseCode: '262516', weeklyPeriods: 3 }])
```

并测试多排课时段不会在课程明细中重复。

- [ ] **Step 2: 验证测试失败**

Run: `npm test -- src/utils/timetable.test.js`

Expected: `normalizeCourseRows is not a function`。

- [ ] **Step 3: 最小实现**

导出：

```js
export function normalizeCourseRows(offerings = []) {
  return [...offerings].sort((a, b) =>
    String(a.courseCode || '').localeCompare(String(b.courseCode || '')))
}
```

- [ ] **Step 4: 验证测试通过**

Run: `npm test -- src/utils/timetable.test.js`

Expected: 测试全部通过。

### Task 4: 课表与花名册主界面

**Files:**
- Create: `JxnuFrontEnd/src/components/schedule/CourseRoster.vue`
- Create: `JxnuFrontEnd/src/components/schedule/CourseDetailsTable.vue`
- Create: `JxnuFrontEnd/src/utils/schedulePresentation.js`
- Modify: `JxnuFrontEnd/src/views/studenthome/schedule.vue`
- Create: `JxnuFrontEnd/src/utils/schedulePresentation.test.js`

- [ ] **Step 1: 写失败测试**

为名单卡片和头像回退逻辑写纯函数测试，避免为现有项目额外引入组件测试依赖：

```js
expect(toRosterCards([
  { studentNo: '202125403035', name: '学生甲', image: '/avatar.jpg' }
])).toEqual([
  { index: 1, studentNo: '202125403035', name: '学生甲', image: '/avatar.jpg' }
])
expect(resolveAvatar('')).toBe(DEFAULT_AVATAR)
```

- [ ] **Step 2: 验证测试失败**

Run: `npm test -- schedulePresentation.test.js`

Expected: `toRosterCards` 和 `resolveAvatar` 不存在。

- [ ] **Step 3: 实现组件和页面交互**

`schedulePresentation.js` 负责名单序号与头像回退；`CourseRoster.vue` 负责四列花名册卡片及返回事件；`CourseDetailsTable.vue` 负责八列课程明细。`schedule.vue`：

- 课程块移除点击事件。
- 点击明细表“查看名单”后加载 `/offerings/{id}/students`。
- 加载成功后切换 `viewMode` 为 `roster`。
- 返回时恢复 `schedule`。
- 切换学期时清空旧名单。
- 讨论和评价入口使用 Vue Router 携带 `offeringId`。

- [ ] **Step 4: 验证组件测试通过**

Run: `npm test -- schedulePresentation.test.js`

Expected: 测试通过。

### Task 5: 全量验证

**Files:**
- Verify all modified files

- [ ] **Step 1: 后端测试**

Run: `.\mvnw.cmd clean test`

Expected: `BUILD SUCCESS`，零失败。

- [ ] **Step 2: 前端测试**

Run: `npm test`

Expected: 所有测试文件通过。

- [ ] **Step 3: 前端生产构建**

Run: `npm run build`

Expected: 退出码 0。

- [ ] **Step 4: 改动范围复核**

Run: `git diff --check` 和 `git status --short`

Expected: 无空白错误；用户已有的 `application.properties.example`、`studenthome/index.vue`、`parentinfo.vue` 状态未被本任务覆盖。
