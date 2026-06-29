import request from '@/utils/request'

//登录
//export const loginApi = (data) => request.post('/login', data)
// 登录学生账号（/stu/login）
export const loginApi = (data) => request.post('/stu/login', data)