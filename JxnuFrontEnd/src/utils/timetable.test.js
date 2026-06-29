import { describe, expect, it } from 'vitest'
import { buildTimetableGrid, normalizeCourseRows } from './timetable'

describe('buildTimetableGrid', () => {
  it('为连续三节课程合并后续单元格', () => {
    const grid = buildTimetableGrid([
      { courseName: 'Web应用技术', schedules: [{ weekday: 4, startPeriod: 1, endPeriod: 3 }] }
    ])
    expect(grid[0][3].rowspan).toBe(3)
    expect(grid[1][3].hidden).toBe(true)
    expect(grid[2][3].hidden).toBe(true)
  })

  it('忽略越界的排课区间', () => {
    const grid = buildTimetableGrid([{ schedules: [{ weekday: 8, startPeriod: 0, endPeriod: 12 }] }])
    expect(grid.flat().every(cell => !cell.hidden && !cell.offering)).toBe(true)
  })

  it('按课程号整理课程明细且不会因多个排课时段重复', () => {
    const web = {
      id: 8,
      courseCode: '262516',
      courseName: 'Web应用技术',
      schedules: [
        { weekday: 4, startPeriod: 1, endPeriod: 2 },
        { weekday: 4, startPeriod: 3, endPeriod: 4 }
      ]
    }
    const history = { id: 1, courseCode: '002926', courseName: '海昏侯研究', schedules: [] }

    expect(normalizeCourseRows([web, history, web]).map(item => item.courseCode))
      .toEqual(['002926', '262516'])
  })
})
