# Jxnu 课程与选课系统接口文档

## 统一约定

- 服务地址：`http://localhost:8082`
- 除 `POST /stu/login` 外，所有接口都必须在请求头携带 `token: <JWT>`。
- 成功响应：`{"code":1,"msg":"success","data":...}`
- 业务失败：`{"code":0,"msg":"中文错误信息","data":null}`
- JWT 缺失或失效时返回 HTTP `401`。

## 登录与学生账户

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/stu/login` | 使用 `username`、`password` 登录并取得 JWT |
| GET | `/student/info` | 查询当前 JWT 对应的学生，不返回密码 |
| POST | `/stu/uploadAvatar` | 上传 `multipart/form-data` 字段 `file` |
| POST | `/stu/changePassword` | 修改当前学生密码 |

修改密码请求包含 `oldPassword` 和 `newPassword`。学生身份始终从 JWT 获取，客户端不得提交学生主键。

## 课程、教师与基础资料

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET/POST | `/courses` | 筛选或新增课程 |
| GET/PUT/DELETE | `/courses/{id}` | 查询、修改或删除课程 |
| GET/POST | `/teachers` | 筛选或新增教师 |
| GET/PUT/DELETE | `/teachers/{id}` | 查询、修改或删除教师 |
| GET | `/semesters` | 学期列表及当前学期标记 |
| GET | `/classes` | 行政班列表 |
| GET | `/classrooms` | 教室列表 |
| GET | `/class-periods` | 第 1 至 11 节的时间 |

课程字段为 `courseCode`、`name`、`courseType`、`credit`、`weeklyPeriods`。`courseType` 取值为 `THEORY`、`EXPERIMENT`、`PRACTICE`。

## 开课与排课

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/offerings` | 按 `semesterId`、`courseName`、`teacherName`、`classId` 筛选 |
| GET | `/offerings/{id}` | 查询开课班及全部排课时段 |
| POST | `/offerings` | 事务新增开课班及排课 |
| PUT | `/offerings/{id}` | 事务修改开课班及排课 |
| DELETE | `/offerings/{id}` | 删除没有选课记录的开课班 |
| GET | `/offerings/{id}/students` | 当前学生查看本人已选开课班的名单 |

名单项返回 `studentNo`、`name`、`className` 和 `image`，其中 `image` 为学生头像地址。

请求示例：

```json
{
  "courseId": 8,
  "teacherId": 3,
  "classId": 2,
  "semesterId": 1,
  "capacity": 60,
  "schedules": [
    {"weekday": 2, "startPeriod": 3, "endPeriod": 4, "classroomId": 5}
  ]
}
```

每段排课必须连续占 2 或 3 节，总节数必须等于课程周课时。系统分别检查同学期、同星期、重叠节次下的教室、教师和行政班冲突。

常见错误：`教室在所选时间段已被占用`、`教师在所选时间段已有课程`、`行政班在所选时间段已有课程`。

## 当前学生选课与个人课表

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/student/selections?semesterId={id}` | 当前学生已选课程 |
| POST | `/student/selections/{offeringId}` | 选择开课班 |
| DELETE | `/student/selections/{offeringId}` | 退出本人已选开课班 |
| GET | `/student/timetable?semesterId={id}` | 当前学生个人课表 |

选课处于事务中，会锁定开课班并检查容量、重复开课班及同一课程的其他开课班。常见错误：`课程容量已满`、`不能重复选择同一开课班`、`同一课程只能选择一个开课班`。

个人课表返回学生、行政班、学期信息，以及包含课程、教师、人数和 `schedules` 的 `courses` 数组。
