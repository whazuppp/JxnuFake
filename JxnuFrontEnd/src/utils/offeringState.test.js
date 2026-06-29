import { describe, expect, it } from 'vitest'
import { businessMessage, selectedOfferingIds, toOfferingParams } from './offeringState'

describe('开课页面状态工具', () => {
  it('只序列化非空筛选条件', () => {
    expect(toOfferingParams({ semesterId: 1, courseName: '', teacherName: '吴', classId: null }))
      .toEqual({ semesterId: 1, teacherName: '吴' })
  })

  it('识别已选开课班编号', () => {
    expect(selectedOfferingIds([{ id: 8 }, { offeringId: 9 }])).toEqual(new Set([8, 9]))
  })

  it('优先使用后端业务提示', () => {
    expect(businessMessage({ msg: '课程容量已满' }, '选课失败')).toBe('课程容量已满')
  })
})
