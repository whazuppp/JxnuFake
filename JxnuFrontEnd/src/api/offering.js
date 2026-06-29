import request from '@/utils/request'
export const queryOfferingsApi = (params) => request.get('/offerings', { params })
export const getOfferingApi = (id) => request.get(`/offerings/${id}`)
export const addOfferingApi = (data) => request.post('/offerings', data)
export const updateOfferingApi = (id, data) => request.put(`/offerings/${id}`, data)
export const deleteOfferingApi = (id) => request.delete(`/offerings/${id}`)
export const queryOfferingStudentsApi = (id) => request.get(`/offerings/${id}/students`)
