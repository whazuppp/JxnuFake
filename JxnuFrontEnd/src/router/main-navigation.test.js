import { existsSync, readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const srcRoot = fileURLToPath(new URL('../', import.meta.url))
const readSource = (path) => readFileSync(new URL(path, import.meta.url), 'utf8')

describe('顶部导航清理', () => {
  it('课程讨论使用外链且评教进入学生之家', () => {
    const layout = readSource('../views/layout/index.vue')
    const router = readSource('./index.js')

    expect(layout).toContain("window.open('https://jwc.jxnu.edu.cn/WsktNew/index.aspx', '_blank')")
    expect(layout).toContain('index="/studenthome/judge">评教')
    expect(layout).not.toMatch(/教学制度|入学导航/)
    expect(router).not.toMatch(/TeachingRulesView|CourseDiscussionView|EvaluationView|NavView/)
    expect(existsSync(`${srcRoot}/views/teaching-rules/index.vue`)).toBe(false)
    expect(existsSync(`${srcRoot}/views/interaction/course-discussion.vue`)).toBe(false)
    expect(existsSync(`${srcRoot}/views/studycenter/evaluation.vue`)).toBe(false)
    expect(existsSync(`${srcRoot}/views/studycenter/nav.vue`)).toBe(false)
  })
})
