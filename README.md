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

## 1. 学生登录接口

### 基本信息

- 请求路径：`/stu/login`
- 前端路径：`/api/stu/login`
- 请求方式：`POST`
- 接口描述：学生账号登录，成功后返回学生信息和 token
- 是否需要 token：否

### 请求参数

- 参数格式：`application/json`

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| stuId | string/number | 是 | `202326202065` | 学号，对应数据库 `stu_id` |
| password | string | 是 | `123456` | 密码 |

请求示例：

```json
{
  "stuId": "202326202065",
  "password": "123456"
}
```

### 响应数据

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 登录成功后的学生信息 |
| data.id | number | 是 | 学生主键 ID |
| data.stuId | string/number | 是 | 学号 |
| data.username | string | 是 | 用户名 |
| data.name | string | 是 | 姓名 |
| data.token | string | 是 | JWT 登录令牌 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 2,
    "stuId": 202326202065,
    "username": "林",
    "name": "林凯",
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

## 2. 查询学生信息接口

### 基本信息

- 请求路径：`/student/info`
- 前端路径：`/api/student/info?id={id}`
- 请求方式：`GET`
- 接口描述：根据学生主键 ID 查询学生信息
- 是否需要 token：是

### 请求参数

- 参数格式：`query string`

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `2` | 学生主键 ID，不是学号 |

请求示例：

```http
GET /student/info?id=2
token: <JWT>
```

### 响应数据

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
    "id": 2,
    "username": "林",
    "password": "123456",
    "name": "林凯",
    "gender": 1,
    "image": "https://example.com/avatar.jpg",
    "entrydate": "2023-09-01",
    "stuId": 202326202065,
    "createTime": "2025-05-26T09:16:13",
    "updateTime": "2025-05-26T09:16:13"
  }
}
```

---

## 3. 上传学生头像接口

### 基本信息

- 请求路径：`/stu/uploadAvatar`
- 前端路径：`/api/stu/uploadAvatar`
- 请求方式：`POST`
- 接口描述：上传学生头像，上传成功后根据 token 中的学生 ID 更新数据库头像地址
- 是否需要 token：是

### 请求参数

- 参数格式：`multipart/form-data`

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| file | file | 是 | 选择图片文件 | 后端参数名必须是 `file` |

请求示例：

```http
POST /stu/uploadAvatar
token: <JWT>
Content-Type: multipart/form-data

file=<图片文件>
```

### 响应数据

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

## 4. 修改密码接口

### 基本信息

- 请求路径：`/stu/changePassword`
- 前端路径：`/api/stu/changePassword`
- 请求方式：`POST`
- 接口描述：修改当前登录学生密码
- 是否需要 token：是

### 请求参数

- 参数格式：`application/json`

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

### 响应数据

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

## 5. 查询课程列表接口

### 基本信息

- 请求路径：`/courses`
- 前端路径：`/api/courses`
- 请求方式：`GET`
- 接口描述：查询课程列表，支持按课程名称、授课教师、每周课时数动态查询
- 是否需要 token：是

### 请求参数

- 参数格式：`query string`
- 参数说明：所有参数都是可选参数；不传参数时查询全部课程

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| name | string | 否 | `Java` | 按课程名称模糊查询 |
| teacherName | string | 否 | `李老师` | 按授课教师模糊查询 |
| number | number | 否 | `4` | 按每周课时数精确查询 |

请求示例：

```http
GET /courses
token: <JWT>
```

```http
GET /courses?name=Java&teacherName=李老师&number=4
token: <JWT>
```

### 响应数据

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

## 6. 查询课程详情接口

### 基本信息

- 请求路径：`/courses/{id}`
- 前端路径：`/api/courses/{id}`
- 请求方式：`GET`
- 接口描述：根据课程 ID 查询课程详情
- 是否需要 token：是

### 请求参数

- 参数格式：`path variable`

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 课程 ID |

请求示例：

```http
GET /courses/1
token: <JWT>
```

### 响应数据

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

## 7. 新增课程接口

### 基本信息

- 请求路径：`/courses`
- 前端路径：`/api/courses`
- 请求方式：`POST`
- 接口描述：新增课程
- 是否需要 token：是

### 请求参数

- 参数格式：`application/json`

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

### 响应数据

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 新增的课程数据 |
| data.id | number | 非必须 | 新增课程 ID，已配置自增 ID 回填 |
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

## 8. 修改课程接口

### 基本信息

- 请求路径：`/courses`
- 前端路径：`/api/courses`
- 请求方式：`PUT`
- 接口描述：修改课程信息
- 是否需要 token：是

### 请求参数

- 参数格式：`application/json`

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 课程 ID |
| name | string | 否 | `Web 前端开发` | 课程名称，不传则不修改 |
| number | number | 否 | `3` | 每周课时数，不传则不修改 |
| teacherName | string | 否 | `王老师` | 授课教师，不传则不修改 |

请求示例：

```json
{
  "id": 1,
  "name": "Web 前端开发",
  "number": 3,
  "teacherName": "王老师"
}
```

### 响应数据

| 参数名 | 类型 | 是否必须 | 备注 |
| --- | --- | --- | --- |
| code | number | 必须 | `1` 成功，`0` 失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 修改后的课程数据 |
| data.id | number | 是 | 课程 ID |
| data.name | string | 非必须 | 课程名称 |
| data.number | number | 非必须 | 每周课时数 |
| data.teacherName | string | 非必须 | 授课教师 |
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

## 9. 删除课程接口

### 基本信息

- 请求路径：`/courses/{id}`
- 前端路径：`/api/courses/{id}`
- 请求方式：`DELETE`
- 接口描述：根据课程 ID 删除课程
- 是否需要 token：是

### 请求参数

- 参数格式：`path variable`

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| --- | --- | --- | --- | --- |
| id | number | 是 | `1` | 课程 ID |

请求示例：

```http
DELETE /courses/1
token: <JWT>
```

### 响应数据

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

## 接口总览

| 模块 | 请求方式 | 请求路径 | 说明 |
| --- | --- | --- | --- |
| 登录 | POST | `/stu/login` | 学生登录 |
| 学生 | GET | `/student/info` | 查询学生信息 |
| 学生 | POST | `/stu/uploadAvatar` | 上传头像 |
| 学生 | POST | `/stu/changePassword` | 修改密码 |
| 课程 | GET | `/courses` | 查询课程列表，支持动态条件查询 |
| 课程 | GET | `/courses/{id}` | 查询课程详情 |
| 课程 | POST | `/courses` | 新增课程 |
| 课程 | PUT | `/courses` | 修改课程 |
| 课程 | DELETE | `/courses/{id}` | 删除课程 |

## Apifox 调试建议

1. 新建环境变量：`baseUrl = http://localhost:8082`。
2. 登录接口请求地址写：`{{baseUrl}}/stu/login`。
3. 登录成功后复制响应里的 `data.token`。
4. 在需要登录的接口请求头中添加：`token: {{token}}`。
5. 头像上传接口的 Body 选择 `form-data`，字段名必须为 `file`，类型选择文件。
