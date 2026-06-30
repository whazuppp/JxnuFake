import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'
import {
  coursesForSemester,
  selectNextSemester
} from './semesterSchedule'

const scheduleView = readFileSync(
  fileURLToPath(new URL('../views/studenthome/schedule.vue', import.meta.url)),
  'utf8'
)
const courseSelectView = readFileSync(
  fileURLToPath(new URL('../views/studycenter/course-select.vue', import.meta.url)),
  'utf8'
)

describe('学期课程规则', () => {
  it('选课中心只保留2026-2027学年第1学期', () => {
    const semesters = [
      { id: 1, academicYear: '2025-2026', termNo: 1 },
      { id: 2, academicYear: '2025-2026', termNo: 2 },
      { id: 3, academicYear: '2026-2027', termNo: 1 }
    ]

    expect(selectNextSemester(semesters)).toEqual([semesters[2]])
  })

  it('所有学期均使用接口课程和真实开课班ID', () => {
    const remoteCourses = [{ id: 99, courseCode: '002174' }]

    expect(coursesForSemester(
      { academicYear: '2025-2026', termNo: 1 },
      remoteCourses
    )).toBe(remoteCourses)
    expect(coursesForSemester(
      { academicYear: '2026-2027', termNo: 1 },
      remoteCourses
    )).toBe(remoteCourses)
  })
})

describe('页面接入学期规则', () => {
  it('课程表使用接口学期数据，选课页锁定未来学期', () => {
    expect(scheduleView).toContain('coursesForSemester')
    expect(courseSelectView).toContain('selectNextSemester')
    expect(courseSelectView).toMatch(/class="semester-select"[\s\S]*disabled/)
  })
})
