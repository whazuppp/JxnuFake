import { describe, expect, it } from 'vitest'
import { businessMessage, selectedOfferingIds, toOfferingParams, toSelectionRows } from './offeringState'

describe('开课页面状态工具', () => {
  it('只序列化非空筛选条件', () => {
    expect(toOfferingParams({ semesterId: 1, courseName: '', teacherName: '吴', classId: null }))
      .toEqual({ semesterId: 1, teacherName: '吴' })
  })

  it('识别已选开课班编号', () => {
    expect(selectedOfferingIds([{ id: 8 }, { offeringId: 9 }])).toEqual(new Set([8, 9]))
  })

  it('按第一学期开课班编号标记已选课程并计算剩余容量', () => {
    const rows = toSelectionRows(
      [{ id: 101, capacity: 60, studentCount: 42 }, { id: 102, capacity: 50, studentCount: 50 }],
      [{ id: 101 }]
    )

    expect(rows[0]).toMatchObject({ id: 101, selected: true, remaining: 18 })
    expect(rows[1]).toMatchObject({ id: 102, selected: false, remaining: 0 })
  })

  it('优先使用后端业务提示', () => {
    expect(businessMessage({ msg: '课程容量已满' }, '选课失败')).toBe('课程容量已满')
    expect(businessMessage({ response: { data: { msg: '第一学期选课失败' } } }, '选课失败'))
      .toBe('第一学期选课失败')
  })
})
