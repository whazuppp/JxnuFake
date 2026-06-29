import request from '@/utils/request'
export const querySemestersApi = () => request.get('/semesters')
export const queryClassesApi = () => request.get('/classes')
export const queryClassroomsApi = () => request.get('/classrooms')
export const queryClassPeriodsApi = () => request.get('/class-periods')
