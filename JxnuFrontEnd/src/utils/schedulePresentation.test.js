import { describe, expect, it } from 'vitest'
import {
  DEFAULT_AVATAR,
  resolveAvatar,
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

  it('头像为空时使用默认头像', () => {
    expect(resolveAvatar('')).toBe(DEFAULT_AVATAR)
    expect(resolveAvatar(null)).toBe(DEFAULT_AVATAR)
    expect(resolveAvatar('/avatar.jpg')).toBe('/avatar.jpg')
  })
})
