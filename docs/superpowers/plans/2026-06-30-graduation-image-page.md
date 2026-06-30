# 毕业生图像采集页面 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为 `/studenthome/graduation` 实现一个接近学校系统风格的毕业生图像采集主界面，包含表格、输入框、说明和禁用按钮。

**Architecture:** 页面只在前端落地，使用 `graduation.vue` 承载本地展示模型与静态结构，复用现有学生布局与本地登录信息。测试通过读取 Vue 文件源码，验证关键业务文案和禁用按钮存在。

**Tech Stack:** Vue 3、Element Plus、Vitest

---

### Task 1: 页面测试先行

**Files:**
- Modify: `JxnuFrontEnd/src/views/prototype-pages.test.js`
- Test: `JxnuFrontEnd/src/views/prototype-pages.test.js`

- [ ] **Step 1: Write the failing test**

新增测试，读取 `studenthome/graduation.vue`，断言源码包含：
`江西师范大学毕业生图像采集信息校对表（本部）`、
`没有找到你的记录！`、
`提交`、
`确认不再更改`、
`disabled`、
`籍贯`、
`手机号`、
`说明`。

- [ ] **Step 2: Run test to verify it fails**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: FAIL，因为当前 `graduation.vue` 为空。

- [ ] **Step 3: Write minimal implementation target**

保持断言只验证页面必须具备的业务骨架，不要求测试渲染行为，也不引入提交逻辑。

- [ ] **Step 4: Run test to verify it passes after implementation**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: PASS。

### Task 2: 实现毕业生图像采集页面

**Files:**
- Modify: `JxnuFrontEnd/src/views/studenthome/graduation.vue`

- [ ] **Step 1: Build local state**

在 `<script setup>` 中创建本地 `student` 与 `form` 状态；`onMounted` 时从 `localStorage.loginUser` 读取学号、姓名及可复用字段，其余默认留空。

- [ ] **Step 2: Render the table layout**

在模板中输出标题、红色提示、四列表格、籍贯输入框、手机号输入框、备注行、两个禁用按钮和说明区。

- [ ] **Step 3: Add minimal styles**

在 `<style scoped>` 中实现白底容器、居中标题、浅灰边框表格、紧凑表单、灰色禁用按钮与移动端横向滚动。

- [ ] **Step 4: Re-run the targeted test**

Run: `npm test -- src/views/prototype-pages.test.js`
Expected: PASS。

### Task 3: 最终验证

- [ ] **Step 1: Run all frontend tests**

Run: `npm test`
Expected: 全部 PASS。

- [ ] **Step 2: Run production build**

Run: `npm run build`
Expected: 构建成功，退出码为 0。
