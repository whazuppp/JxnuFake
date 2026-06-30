import request from '@/utils/request'
export const querySelectionsApi = (semesterId) => request.get('/student/selections', { params: { semesterId } })
export const selectOfferingApi = (id) => request.post(`/student/selections/${id}`)
export const withdrawOfferingApi = (id) => request.delete(`/student/selections/${id}`)
export const queryTimetableApi = (semesterId) => request.get('/student/timetable', { params: { semesterId } })
