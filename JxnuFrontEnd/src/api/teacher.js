import request from '@/utils/request'
export const queryTeachersApi = (params) => request.get('/teachers', { params })
export const addTeacherApi = (data) => request.post('/teachers', data)
export const updateTeacherApi = (id, data) => request.put(`/teachers/${id}`, data)
export const deleteTeacherApi = (id) => request.delete(`/teachers/${id}`)
