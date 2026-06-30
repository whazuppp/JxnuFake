import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const judgeView = readFileSync(
  fileURLToPath(new URL('./judge.vue', import.meta.url)),
  'utf8'
)
const scheduleView = readFileSync(
  fileURLToPath(new URL('./schedule.vue', import.meta.url)),
  'utf8'
)

describe('网上评教页面', () => {
  it('使用学期接口与课程表接口驱动评教课程切换', () => {
    expect(judgeView).toContain('querySemestersApi')
    expect(judgeView).toContain('queryTimetableApi')
    expect(judgeView).toContain('pendingSemesterId')
    expect(judgeView).toContain('applySemesterSelection')
    expect(judgeView).toContain('课程名称标识')
    expect(judgeView).toContain('教师语义评价窗口')
  })

  it('支持从课程表进入指定课程的语义评价窗口', () => {
    expect(judgeView).toContain('route.query.offeringId')
    expect(judgeView).toContain('openEvaluationById')
    expect(scheduleView).toContain("path: '/studenthome/judge'")
    expect(scheduleView).toContain('semesterId: offering.semesterId || semesterId.value')
  })
})
