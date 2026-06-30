import request from "@/utils/request"

// 查询所有课程列表
export const queryAllCoursesApi = (params) => request.get('/courses', { params })

// 添加课程
export const addCourseApi = (data) => request.post('/courses', data)

// 根据 ID 查询课程
export const queryCourseByIdApi = (id) => request.get(`/courses/${id}`)

// 修改课程信息
export const updateCourseApi = (id, data) => request.put(`/courses/${id}`, data)

// 删除课程
export const deleteCourseApi = (id) => request.delete(`/courses/${id}`)
