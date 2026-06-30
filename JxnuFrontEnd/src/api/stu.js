import request from '@/utils/request'

// 查询当前学生信息，id 是学生主键 ID，不是学号 stuId
export const getStudentInfoApi = () => request.get('/student/info')

// 查询学生信息列表
export const queryStudentsApi = (params) => request.get('/students', { params })

// 上传头像，后端接收字段名必须是 file
export const uploadAvatarApi = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/stu/uploadAvatar', formData)
}

// 修改当前登录学生密码
export const changePasswordApi = (data) =>
  request.post('/stu/changePassword', data)
