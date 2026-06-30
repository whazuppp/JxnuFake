import { describe, expect, it } from 'vitest'
import {
  DEFAULT_AVATAR,
  resolveAvatar,
  resolveStudentAvatar,
  toRosterCards
} from './schedulePresentation'

describe('课程花名册展示数据', () => {
  it('为学生添加稳定序号并保留头像信息', () => {
    expect(toRosterCards([
      {
        studentNo: '202125403035',
        name: '学生甲',
        className: '23级计算机科学与技术2班',
        image: '/avatar.jpg'
      }
    ])).toEqual([
      {
        index: 1,
        studentNo: '202125403035',
        name: '学生甲',
        className: '23级计算机科学与技术2班',
        image: '/avatar.jpg'
      }
    ])
  })

  it('兼容不同后端头像字段，确保班级名单能显示头像', () => {
    expect(resolveStudentAvatar({ avatarUrl: '/avatar-from-api.jpg' })).toBe('/avatar-from-api.jpg')
    expect(resolveStudentAvatar({ photoUrl: '/photo-from-api.jpg' })).toBe('/photo-from-api.jpg')
  })

  it('头像为空时使用默认头像', () => {
    expect(resolveAvatar('')).toBe(DEFAULT_AVATAR)
    expect(resolveAvatar(null)).toBe(DEFAULT_AVATAR)
    expect(resolveAvatar('/avatar.jpg')).toBe('/avatar.jpg')
  })
})
