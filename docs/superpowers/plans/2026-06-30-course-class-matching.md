# 选课班级匹配 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 同课程按1班和2班区分排课，并拒绝学生选择非本班开课班。

**Architecture:** 后端服务在插入选课记录前比较学生与开课班的 `classId`；前端沿用现有业务错误展示。未来学期种子固定创建三门课程的两个班级，并为每班配置不同排课。

**Tech Stack:** Spring Boot、JUnit 5、Mockito、MySQL、Vue 3、Vitest

---

### Task 1: 后端班级匹配校验

**Files:**
- Modify: `JxnuBackEnd/src/test/java/com/sf110/jxnufake/service/impl/StudentCourseServiceImplTest.java`
- Modify: `JxnuBackEnd/src/main/java/com/sf110/jxnufake/service/impl/StudentCourseServiceImpl.java`

- [ ] **Step 1: Write the failing test**

新增测试：学生 `classId=2`，开课班 `classId=1`，调用 `select(7, 12)` 后抛出
`BusinessException("所选课程与班级不匹配")`，且不调用 `insert`。

- [ ] **Step 2: Run test to verify it fails**

Run: `mvnw.cmd -Dtest=StudentCourseServiceImplTest test`
Expected: FAIL，因为服务尚未校验班级。

- [ ] **Step 3: Write minimal implementation**

在开课班存在性校验后读取 `stuMapper.getInfoById(studentId)`；学生不存在时抛出“学生不存在”，
`student.classId()` 与 `offering.getClassId()` 不一致时抛出“所选课程与班级不匹配”。

- [ ] **Step 4: Run test to verify it passes**

Run: `mvnw.cmd -Dtest=StudentCourseServiceImplTest test`
Expected: PASS。

### Task 2: 未来学期分班数据

**Files:**
- Modify: `sql/06-seed-2026-first-semester.sql`
- Modify: `JxnuFrontEnd/src/utils/sqlSeed.test.js`

- [ ] **Step 1: Write the failing SQL test**

断言未来学期种子包含 `CS2023-01`、`CS2023-02`，每门课程生成两个开课班，并包含班级不同的排课定义。

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/utils/sqlSeed.test.js`
Expected: FAIL，因为当前种子只复制历史开课班。

- [ ] **Step 3: Write minimal SQL**

清理尚无学生选课的未来学期重复开课班；固定创建 Web应用技术、数据库系统、Java程序设计的1班和2班；
使用稳定教师号，每班分别插入不同 weekday、period 和 classroom。

- [ ] **Step 4: Run test to verify it passes**

Run: `npm test -- src/utils/sqlSeed.test.js`
Expected: PASS。

### Task 3: 数据库与完整验证

- [ ] **Step 1: Apply seed**

Run: `mysql --default-character-set=utf8mb4 -uroot -p1234 jxnu < sql/06-seed-2026-first-semester.sql`
Expected: 未来学期共 6 个开课班。

- [ ] **Step 2: Verify database**

查询应显示三门课程各有1班和2班、同课程排课不同、学生预选数为0。

- [ ] **Step 3: Run backend tests**

Run: `mvnw.cmd test`
Expected: 全部 PASS。

- [ ] **Step 4: Run frontend tests and build**

Run: `npm test`，再运行 `npm run build`
Expected: 全部 PASS 且构建退出码为0。
