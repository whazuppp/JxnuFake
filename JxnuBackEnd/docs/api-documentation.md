# Jxnu 项目接口文档

## 统一说明

- 后端服务地址：`http://localhost:8082`
- 前端代理前缀：`/api`
- 前端请求示例：`/api/stu/login`
- 后端实际接口：`/stu/login`
- 除登录接口外，其余接口均需要请求头：`token`

| 请求头名称 | 示例 | 说明 |
| --- | --- | --- |
| token | 登录接口返回的 JWT | 登录成功后获得，后续接口必须携带 |

## 统一响应格式

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | 响应码，`1` 表示成功，`0` 表示失败 |
| msg | string | 非必须 | 提示信息 |
| data | object/string/array/null | 非必须 | 返回数据 |

响应示例：

```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

# 1. 学生登录接口

## 基本信息

- 请求路径：`/stu/login`
- 前端路径：`/api/stu/login`
- 请求方式：`POST`
- 前端方法：`loginApi(data)`
- 接口描述：学生账号登录，成功后返回学生信息和 token
- 是否需要 token：否

## 请求参数

- 参数格式：`application/json`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| stuId | string/number | 是 | `20240001` | 学号，后端通过 `stu_id` 查询 |
| password | string | 是 | `123456` | 密码 |

请求示例：

```json
{
  "stuId": "20240001",
  "password": "123456"
}
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 登录成功后的学生信息 |
| data.id | number | 是 | 学生主键 ID |
| data.username | string | 是 | 用户名 |
| data.name | string | 是 | 姓名 |
| data.stuId | string | 是 | 学号 |
| data.token | string | 是 | JWT 登录令牌 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "name": "张三",
    "stuId": "20240001",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

失败响应样例：

```json
{
  "code": 0,
  "msg": "用户名或密码错误",
  "data": null
}
```

---

# 2. 查询学生信息接口

## 基本信息

- 请求路径：`/student/info`
- 前端路径：`/api/student/info?id={id}`
- 请求方式：`GET`
- 前端方法：`getStudentInfoApi(id)`
- 接口描述：根据学生主键 ID 查询学生信息
- 是否需要 token：是

## 请求参数

- 参数格式：`query string`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 学生主键 ID，不是学号 |

请求示例：

```http
GET /student/info?id=1
token: <JWT>
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 学生信息 |
| data.id | number | 是 | 学生主键 ID |
| data.username | string | 非必须 | 用户名 |
| data.password | string | 非必须 | 密码，当前后端会返回，前端不建议展示 |
| data.name | string | 非必须 | 姓名 |
| data.gender | number | 非必须 | 性别，通常 `1` 男，`2` 女 |
| data.image | string | 非必须 | 头像地址 |
| data.entrydate | string | 非必须 | 入学日期 |
| data.stuId | number | 非必须 | 学号 |
| data.createTime | string | 非必须 | 创建时间 |
| data.updateTime | string | 非必须 | 更新时间 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "password": "123456",
    "name": "张三",
    "gender": 1,
    "image": "https://example.com/avatar.jpg",
    "entrydate": "2024-09-01",
    "stuId": 20240001,
    "createTime": "2024-09-01T10:00:00",
    "updateTime": "2024-09-01T10:00:00"
  }
}
```

---

# 3. 上传学生头像接口

## 基本信息

- 请求路径：`/stu/uploadAvatar`
- 前端路径：`/api/stu/uploadAvatar`
- 请求方式：`POST`
- 前端方法：`uploadAvatarApi(file)`
- 接口描述：上传学生头像，上传成功后更新数据库头像地址
- 是否需要 token：是

## 请求参数

- 参数格式：`multipart/form-data`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| file | file | 是 | 选择图片文件 | 后端参数名必须是 `file`，前端限制 JPG/PNG 且小于 10MB |

请求示例：

```http
POST /stu/uploadAvatar
token: <JWT>
Content-Type: multipart/form-data

file=<图片文件>
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | string | 非必须 | 上传后的头像访问地址 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": "https://oss-cn-beijing.aliyuncs.com/xxx/avatar.png"
}
```

---

# 4. 修改密码接口

## 基本信息

- 请求路径：`/stu/changePassword`
- 前端路径：`/api/stu/changePassword`
- 请求方式：`POST`
- 前端方法：`changePasswordApi(data)`
- 接口描述：修改当前登录学生密码
- 是否需要 token：是

## 请求参数

- 参数格式：`application/json`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| oldPassword | string | 是 | `123456` | 原密码 |
| newPassword | string | 是 | `654321` | 新密码 |

请求示例：

```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | string/null | 非必须 | 修改结果 |

成功响应样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": "修改成功"
}
```

失败响应样例：

```json
{
  "code": 0,
  "msg": "原密码错误",
  "data": null
}
```

---

# 5. 查询课程列表接口

## 基本信息

- 请求路径：`/courses`
- 前端路径：`/api/courses`
- 请求方式：`GET`
- 前端方法：`queryAllCoursesApi()`
- 接口描述：查询所有课程列表
- 是否需要 token：是

## 请求参数

- 参数格式：无
- 参数说明：无请求参数

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | array | 非必须 | 课程列表 |
| data[].id | number | 是 | 课程 ID |
| data[].name | string | 是 | 课程名称 |
| data[].number | number | 是 | 每周课时数 |
| data[].teacherName | string | 是 | 授课教师 |
| data[].createTime | string | 非必须 | 创建时间 |
| data[].updateTime | string | 非必须 | 更新时间 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "name": "Java 程序设计",
      "number": 4,
      "teacherName": "李老师",
      "createTime": "2024-09-01T10:00:00",
      "updateTime": "2024-09-01T10:00:00"
    }
  ]
}
```

---

# 6. 查询课程详情接口

## 基本信息

- 请求路径：`/courses/{id}`
- 前端路径：`/api/courses/{id}`
- 请求方式：`GET`
- 前端方法：`queryCourseByIdApi(id)`
- 接口描述：根据课程 ID 查询课程详情
- 是否需要 token：是

## 请求参数

- 参数格式：`path variable`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 课程 ID |

请求示例：

```http
GET /courses/1
token: <JWT>
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object/null | 非必须 | 课程详情，课程不存在时可能为 `null` |
| data.id | number | 是 | 课程 ID |
| data.name | string | 是 | 课程名称 |
| data.number | number | 是 | 每周课时数 |
| data.teacherName | string | 是 | 授课教师 |
| data.createTime | string | 非必须 | 创建时间 |
| data.updateTime | string | 非必须 | 更新时间 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "name": "Java 程序设计",
    "number": 4,
    "teacherName": "李老师",
    "createTime": "2024-09-01T10:00:00",
    "updateTime": "2024-09-01T10:00:00"
  }
}
```

---

# 7. 新增课程接口

## 基本信息

- 请求路径：`/courses`
- 前端路径：`/api/courses`
- 请求方式：`POST`
- 前端方法：`addCourseApi(data)`
- 接口描述：新增课程
- 是否需要 token：是

## 请求参数

- 参数格式：`application/json`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| name | string | 是 | `Java 程序设计` | 课程名称 |
| number | number | 是 | `4` | 每周课时数 |
| teacherName | string | 是 | `李老师` | 授课教师 |

请求示例：

```json
{
  "name": "Java 程序设计",
  "number": 4,
  "teacherName": "李老师"
}
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 新增的课程数据 |
| data.name | string | 是 | 课程名称 |
| data.number | number | 是 | 每周课时数 |
| data.teacherName | string | 是 | 授课教师 |
| data.createTime | string | 非必须 | 后端自动生成 |
| data.updateTime | string | 非必须 | 后端自动生成 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "name": "Java 程序设计",
    "number": 4,
    "teacherName": "李老师",
    "createTime": "2024-09-01T10:00:00",
    "updateTime": "2024-09-01T10:00:00"
  }
}
```

备注：当前后端 Mapper 未配置自增 ID 回填，新增成功后的 `data.id` 可能为空。

---

# 8. 修改课程接口

## 基本信息

- 请求路径：`/courses`
- 前端路径：`/api/courses`
- 请求方式：`PUT`
- 前端方法：`updateCourseApi(data)`
- 接口描述：修改课程信息
- 是否需要 token：是

## 请求参数

- 参数格式：`application/json`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 课程 ID |
| name | string | 是 | `Web 前端开发` | 课程名称 |
| number | number | 是 | `3` | 每周课时数 |
| teacherName | string | 是 | `王老师` | 授课教师 |
| createTime | string | 建议传 | `2024-09-01T10:00:00` | 当前后端 SQL 会更新 `create_time` |

请求示例：

```json
{
  "id": 1,
  "name": "Web 前端开发",
  "number": 3,
  "teacherName": "王老师",
  "createTime": "2024-09-01T10:00:00"
}
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 修改后的课程数据 |
| data.id | number | 是 | 课程 ID |
| data.name | string | 是 | 课程名称 |
| data.number | number | 是 | 每周课时数 |
| data.teacherName | string | 是 | 授课教师 |
| data.updateTime | string | 非必须 | 后端自动更新 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "name": "Web 前端开发",
    "number": 3,
    "teacherName": "王老师",
    "updateTime": "2024-09-01T11:00:00"
  }
}
```

---

# 9. 删除课程接口

## 基本信息

- 请求路径：`/courses/{id}`
- 前端路径：`/api/courses/{id}`
- 请求方式：`DELETE`
- 前端方法：`deleteCourseApi(id)`
- 接口描述：根据课程 ID 删除课程
- 是否需要 token：是

## 请求参数

- 参数格式：`path variable`
- 参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 课程 ID |

请求示例：

```http
DELETE /courses/1
token: <JWT>
```

## 响应数据

- 参数格式：`application/json`
- 参数说明：

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | null | 非必须 | 删除成功时为空 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

# 接口总览

| 模块 | 请求方式 | 请求路径 | 前端方法 | 说明 |
| --- | --- | --- | --- | --- |
| 登录 | POST | `/stu/login` | `loginApi` | 学生登录 |
| 学生 | GET | `/student/info` | `getStudentInfoApi` | 查询学生信息 |
| 学生 | POST | `/stu/uploadAvatar` | `uploadAvatarApi` | 上传头像 |
| 学生 | POST | `/stu/changePassword` | `changePasswordApi` | 修改密码 |
| 课程 | GET | `/courses` | `queryAllCoursesApi` | 查询课程列表 |
| 课程 | GET | `/courses/{id}` | `queryCourseByIdApi` | 查询课程详情 |
| 课程 | POST | `/courses` | `addCourseApi` | 新增课程 |
| 课程 | PUT | `/courses` | `updateCourseApi` | 修改课程 |
| 课程 | DELETE | `/courses/{id}` | `deleteCourseApi` | 删除课程 |
