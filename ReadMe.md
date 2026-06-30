# Jxnu 规范化课程与个人课表设计

## 1. 目标

将当前“课程表直接保存教师姓名”的结构改为至少满足第三范式的教学模型，并完成数据库、Spring Boot 3、Vue 3、接口文档和前后端联调。

最终学生端应支持：

- 按学期查看星期一至星期日、第 1 至 11 节的个人课表。
- 一个开课班每周可有多个时间段，每段连续占用 2 或 3 节。
- 查看课程号、课程名称、课程类型、学分、周课时、行政班、教师工号和姓名、上课时间、教学楼及教室号。
- 选课、退课，以及查看同一开课班的选课学生名单。

## 2. 已确认业务规则

1. 同一学期、星期和重叠节次内，同一个教室只能安排一个开课班。
2. 同一学期、星期和重叠节次内，同一个教师只能讲授一个开课班。
3. 同一学期、星期和重叠节次内，同一个行政班只能安排一个开课班。
4. 同一个学生对同一门课程只能选择一个开课班，即不能同时选择不同教师讲授的同一课程。
5. 查看开课班学生名单前，当前学生必须已经选择该开课班。
6. 允许清空旧课程相关表并使用新的建表脚本和测试数据，不做旧数据迁移。
7. 星期范围为星期一至星期日。
8. 每节 40 分钟，课间休息 10 分钟：

| 节次 | 开始 | 结束 | 时段 |
|---|---|---|---|
| 1 | 08:00 | 08:40 | 上午 |
| 2 | 08:50 | 09:30 | 上午 |
| 3 | 09:40 | 10:20 | 上午 |
| 4 | 10:30 | 11:10 | 上午 |
| 5 | 11:20 | 12:00 | 上午 |
| 6 | 14:00 | 14:40 | 下午 |
| 7 | 14:50 | 15:30 | 下午 |
| 8 | 15:40 | 16:20 | 下午 |
| 9 | 16:30 | 17:10 | 下午 |
| 10 | 19:00 | 19:40 | 晚上 |
| 11 | 19:50 | 20:30 | 晚上 |

## 3. 数据库设计

数据库采用 MySQL 8，所有业务表使用自增 `id` 作为内部主键，学号、教师工号、课程号等业务编号使用唯一索引。

### 3.1 基础资料表

#### `administrative_class`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT | 班级主键 |
| class_code | VARCHAR(30) | NOT NULL, UNIQUE | 班级编号 |
| class_name | VARCHAR(100) | NOT NULL | 班级名称 |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 修改时间 |

#### `student`

保留现有登录、头像和密码字段，将学生所属班级改为外键。

| 关键字段 | 约束 |
|---|---|
| id | PK, AUTO_INCREMENT |
| username | NOT NULL, UNIQUE |
| password | NOT NULL |
| name | NOT NULL |
| gender | NOT NULL, CHECK IN (1, 2) |
| image | NULL |
| entrydate | NULL |
| stu_id | NOT NULL, UNIQUE |
| class_id | NOT NULL, FK -> administrative_class.id |
| create_time / update_time | NOT NULL |

#### `teacher`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT | 教师内部主键 |
| teacher_no | VARCHAR(30) | NOT NULL, UNIQUE | 教师工号 |
| name | VARCHAR(50) | NOT NULL | 姓名 |
| gender | TINYINT UNSIGNED | NULL, CHECK IN (1, 2) | 性别 |
| title | VARCHAR(50) | NULL | 职称 |
| create_time / update_time | DATETIME | NOT NULL | 时间戳 |

#### `course`

教师不属于课程固有属性，因此不再保存 `teacher_name`。

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT | 课程主键 |
| course_code | VARCHAR(30) | NOT NULL, UNIQUE | 课程号 |
| name | VARCHAR(100) | NOT NULL | 课程名称 |
| course_type | VARCHAR(20) | NOT NULL | THEORY、EXPERIMENT 或 PRACTICE |
| credit | DECIMAL(3,1) | NOT NULL, CHECK > 0 | 学分 |
| weekly_periods | TINYINT UNSIGNED | NOT NULL, CHECK BETWEEN 2 AND 21 | 每周课时 |
| create_time / update_time | DATETIME | NOT NULL | 时间戳 |

#### `semester`

| 字段 | 类型 | 约束 |
|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT |
| academic_year | VARCHAR(20) | NOT NULL |
| term_no | TINYINT UNSIGNED | NOT NULL, CHECK IN (1, 2, 3) |
| name | VARCHAR(50) | NOT NULL |
| start_date / end_date | DATE | NOT NULL |
| is_current | BOOLEAN | NOT NULL DEFAULT FALSE |
| academic_year + term_no | UNIQUE |

同一时刻只允许一个 `is_current = TRUE`，由服务层事务控制。

#### `classroom`

| 字段 | 类型 | 约束 |
|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT |
| building | VARCHAR(100) | NOT NULL |
| room_no | VARCHAR(30) | NOT NULL |
| capacity | INT UNSIGNED | NOT NULL, CHECK > 0 |
| building + room_no | UNIQUE |

#### `class_period`

| 字段 | 类型 | 约束 |
|---|---|---|
| period_no | TINYINT UNSIGNED | PK, CHECK BETWEEN 1 AND 11 |
| day_part | VARCHAR(10) | NOT NULL |
| start_time | TIME | NOT NULL |
| end_time | TIME | NOT NULL |

### 3.2 教学业务表

#### `course_offering`

表示“某学期由某教师给某行政班开设某门课程”。

| 字段 | 类型 | 约束 |
|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT |
| course_id | INT UNSIGNED | NOT NULL, FK -> course.id |
| teacher_id | INT UNSIGNED | NOT NULL, FK -> teacher.id |
| class_id | INT UNSIGNED | NOT NULL, FK -> administrative_class.id |
| semester_id | INT UNSIGNED | NOT NULL, FK -> semester.id |
| capacity | INT UNSIGNED | NOT NULL, CHECK > 0 |
| create_time / update_time | DATETIME | NOT NULL |

`course_id + teacher_id + class_id + semester_id` 建立唯一索引。

#### `course_schedule`

| 字段 | 类型 | 约束 |
|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT |
| offering_id | INT UNSIGNED | NOT NULL, FK -> course_offering.id |
| weekday | TINYINT UNSIGNED | NOT NULL, CHECK BETWEEN 1 AND 7 |
| start_period | TINYINT UNSIGNED | NOT NULL, FK -> class_period.period_no |
| end_period | TINYINT UNSIGNED | NOT NULL, FK -> class_period.period_no |
| classroom_id | INT UNSIGNED | NOT NULL, FK -> classroom.id |

`end_period - start_period + 1` 必须为 2 或 3。一个开课班全部排课时段的课时之和必须等于课程的 `weekly_periods`。

#### `student_course`

| 字段 | 类型 | 约束 |
|---|---|---|
| id | INT UNSIGNED | PK, AUTO_INCREMENT |
| student_id | INT UNSIGNED | NOT NULL, FK -> student.id |
| offering_id | INT UNSIGNED | NOT NULL, FK -> course_offering.id |
| score | DECIMAL(5,2) | NULL, CHECK BETWEEN 0 AND 100 |
| create_time / update_time | DATETIME | NOT NULL |

`student_id + offering_id` 建立唯一索引。“同一学生同一课程只能选一个开课班”需要跨表判断，由服务层事务检查并由数据库触发器兜底。

### 3.3 第三范式说明

- 教师资料只保存在 `teacher`，课程表和排课表不重复保存教师姓名或工号。
- 行政班名称只保存在 `administrative_class`。
- 学期名称只保存在 `semester`。
- 教室位置只保存在 `classroom`。
- 课程名称、类型、学分和周课时只保存在 `course`。
- 排课表只记录开课班、星期、节次和教室；接口展示字段通过关联查询获得。
- `student_course` 不重复保存 `course_id`、`teacher_id` 或 `semester_id`。

## 4. 后端接口契约

统一后端地址为 `http://localhost:8082`，Vite 开发代理前缀为 `/api`。除登录外均携带请求头 `token`。

统一响应：

```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

### 4.1 学生账户

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/stu/login` | 学号和密码登录 |
| GET | `/student/info` | 从 token 获取当前学生资料，不再接受学生 ID |
| POST | `/stu/uploadAvatar` | 上传当前学生头像 |
| POST | `/stu/changePassword` | 修改当前学生密码 |

### 4.2 课程

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/courses` | 按 courseCode、name、courseType 动态查询 |
| GET | `/courses/{id}` | 查询课程 |
| POST | `/courses` | 新增课程 |
| PUT | `/courses/{id}` | 修改课程 |
| DELETE | `/courses/{id}` | 删除未被开课班引用的课程 |

课程 JSON：

```json
{
  "id": 8,
  "courseCode": "262516",
  "name": "Web应用技术",
  "courseType": "THEORY",
  "credit": 3.0,
  "weeklyPeriods": 3
}
```

### 4.3 教师

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/teachers` | 按 teacherNo、name 动态查询 |
| GET | `/teachers/{id}` | 查询教师 |
| POST | `/teachers` | 新增教师 |
| PUT | `/teachers/{id}` | 修改教师 |
| DELETE | `/teachers/{id}` | 删除未被开课班引用的教师 |

### 4.4 学期、班级和教室

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/semesters` | 查询学期，返回当前学期标记 |
| GET | `/classes` | 查询行政班 |
| GET | `/classrooms` | 查询教室 |
| GET | `/class-periods` | 查询第 1 至 11 节及起止时间 |

上述资料由建表脚本插入演示数据，本轮前端不提供维护页面。

### 4.5 开课与排课

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/offerings` | 按 semesterId、courseName、teacherName、classId 查询开课班 |
| GET | `/offerings/{id}` | 查询开课班和全部排课时段 |
| POST | `/offerings` | 事务新增开课班和排课时段 |
| PUT | `/offerings/{id}` | 事务修改开课班和排课时段 |
| DELETE | `/offerings/{id}` | 删除无选课记录的开课班 |
| GET | `/offerings/{id}/students` | 查看同一开课班的选课学生 |

新增或修改请求：

```json
{
  "courseId": 8,
  "teacherId": 3,
  "classId": 2,
  "semesterId": 1,
  "capacity": 60,
  "schedules": [
    {
      "weekday": 2,
      "startPeriod": 3,
      "endPeriod": 4,
      "classroomId": 5
    },
    {
      "weekday": 4,
      "startPeriod": 1,
      "endPeriod": 3,
      "classroomId": 7
    }
  ]
}
```

保存过程必须处于同一事务：

1. 校验课程、教师、班级、学期和教室存在。
2. 校验每个时间段位于第 1 至 11 节且连续占 2 或 3 节。
3. 校验全部时间段课时之和等于课程每周课时。
4. 使用区间条件 `existing.start_period <= new.end_period AND existing.end_period >= new.start_period` 检查同学期、同星期的教室冲突。
5. 使用同一区间条件检查教师冲突。
6. 使用同一区间条件检查行政班冲突。
7. 任一校验失败则回滚并返回明确中文错误。

### 4.6 当前学生选课和课表

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/student/selections?semesterId={id}` | 当前学生某学期已选开课班 |
| POST | `/student/selections/{offeringId}` | 当前学生选课 |
| DELETE | `/student/selections/{offeringId}` | 当前学生退课 |
| GET | `/student/timetable?semesterId={id}` | 当前学生个人课表 |

学生 ID 均从 token 获取。选课时检查容量，以及当前学生是否已选择同一 `course_id` 的其他开课班。

个人课表响应核心结构：

```json
{
  "studentId": 2,
  "studentNo": "202326202065",
  "studentName": "林凯",
  "className": "23级计算机科学与技术2班",
  "semesterId": 1,
  "semesterName": "2025-2026学年第2学期",
  "courses": [
    {
      "offeringId": 12,
      "courseCode": "262516",
      "courseName": "Web应用技术",
      "courseType": "THEORY",
      "credit": 3.0,
      "weeklyPeriods": 3,
      "teacherNo": "T003",
      "teacherName": "吴水秀",
      "className": "23级计算机科学与技术2班",
      "studentCount": 36,
      "schedules": [
        {
          "weekday": 4,
          "startPeriod": 1,
          "endPeriod": 3,
          "startTime": "08:00",
          "endTime": "10:20",
          "building": "先骕楼",
          "roomNo": "X4313g"
        }
      ]
    }
  ]
}
```

开课班学生名单仅返回：

```json
[
  {
    "studentNo": "202326202065",
    "name": "林凯",
    "className": "23级计算机科学与技术2班",
    "image": "https://example.com/avatar.jpg"
  }
]
```

## 5. Spring Boot 3 设计

- Controller 只负责 HTTP 参数和统一响应。
- Service 负责事务、冲突检查、权限检查和业务错误。
- Mapper 接口不写 SQL 注解，所有动态查询保存在 `src/main/resources/mapper/*.xml`。
- 数据库实体与接口 DTO 分离，避免将密码或数据库字段直接返回给前端。
- 新增统一业务异常和全局异常处理，将冲突、重复选课、容量不足和无权查看转为 `Result.error(msg)`。
- 排课冲突查询和写入处于同一事务；数据库脚本提供外键、唯一索引、`CHECK` 和触发器兜底。

## 6. Vue 3 设计

### `src/api`

- `course.js`：课程目录接口。
- `teacher.js`：教师接口。
- `offering.js`：开课、排课和学生名单接口。
- `studentCourse.js`：当前学生选课与个人课表接口。
- `stu.js`：学生账户接口，其中 `getStudentInfoApi()` 不再接收 ID。

### 页面

#### `studenthome/schedule.vue`

- 页面标题为“江西师范大学学生课程表”。
- 显示当前学生班级、学号和姓名。
- 学期下拉框默认当前学期。
- 七列表示星期一至星期日，十一行表示第 1 至 11 节。
- 连续 2 或 3 节使用 `rowspan` 合并课程单元格。
- 课程块显示课程名、教室和班级，课程块本身不触发名单操作。
- 窄屏保持最小表格宽度并横向滚动，避免文字被压缩或重叠。
- 下方课程明细显示课程号、名称标识、周总课时、班级、任课老师、班级同学、讨论区和评价入口。
- 点击“查看名单”后在主内容区切换为四列花名册，展示头像、学号、姓名和行政班，并可返回课程表。

#### `studenthome/timetable.vue`

- 展示当前学期全部开课安排。
- 支持按课程名、教师和班级筛选。
- 提供选课和退课按钮。
- 选课失败时直接展示后端返回的重复课程、容量等错误。

#### `studenthome/courseinfo.vue`

- 展示课程目录的课程号、名称、类型、学分和每周课时。
- 不再展示 `teacherName`，教师属于开课班。

## 7. 接口文档产物

在实现代码前先生成并评审接口契约：

- `docs/api-documentation.md`：直接明了的中文接口文档。
- `docs/Jxnu.apifox.openapi.json`：可导入 Apifox 的 OpenAPI 3 JSON。

代码和联调完成后生成：

- `output/pdf/Jxnu-前后端接口文档.pdf`：替代旧版 `D:\XiangmuReport\接口文档.pdf` 的新版 PDF。

新版 PDF 延续旧文档的“基本信息、请求参数、响应数据、示例”结构，并增加数据库关系说明、前端调用方法、冲突错误示例和接口总览。

## 8. 测试与联调标准

### 后端自动化测试

- 每种排课冲突分别有失败测试：教室、教师、行政班。
- 相邻但不重叠的节次允许保存。
- 2 节和 3 节连续时段允许保存，其他长度拒绝。
- 总排课数与课程每周课时不一致时拒绝。
- 同一学生选择同一课程的不同教师开课班时拒绝。
- 未选某开课班时禁止查看其学生名单。
- 已选课程的个人课表返回完整课程、教师、班级、时间和教室字段。
- 旧学生登录、头像和修改密码功能保持通过。

### 前端验证

- `npm run build` 成功。
- 无数据、加载中和请求失败均有明确状态。
- 桌面端完整显示七天课表。
- 小屏页面可横向滚动，无重叠、截断和布局跳动。
- 连续课程块跨行正确，上午、下午、晚上分区清楚。

### 联调

1. 执行全新数据库脚本和测试数据。
2. 启动 Spring Boot 3，端口 `8082`。
3. 启动 Vite，通过 `/api` 代理访问后端。
4. 学生登录并获取 token。
5. 完成课程查询、开课查询、选课、个人课表、查看同学、退课流程。
6. 使用 Apifox 导入 OpenAPI JSON，验证正常流程和全部冲突响应。

## 9. 非目标

- 本轮不实现教师登录或管理员角色权限系统。
- 本轮不提供学期、行政班、教室和节次的前端维护页面。
- 本轮不迁移旧课程和选课数据。
- 本轮不实现考试安排、成绩录入或复杂跨班合班教学。
