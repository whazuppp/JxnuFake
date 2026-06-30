import { existsSync, readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const srcRoot = fileURLToPath(new URL('../', import.meta.url))
const readSource = (path) => readFileSync(new URL(path, import.meta.url), 'utf8')

describe('教学组织导航', () => {
  it('只保留机构职能和外部审核评估入口', () => {
    const layout = readSource('../views/layout/index.vue')
    const router = readSource('./index.js')
    const removedPages = [
      'monitoring.vue',
      'major-assess.vue',
      'project-review.vue',
      'audit.vue',
      'data-collect.vue'
    ]

    expect(layout).toContain("window.open('https://jxpj.jxnu.edu.cn/', '_blank')")
    expect(layout).not.toMatch(/常态监控|专业评估|项目评审|状态数据采集/)
    expect(router).not.toMatch(/MonitoringView|MajorAssessView|ProjectReviewView|AuditView|DataCollectView/)
    expect(removedPages.every((page) => !existsSync(`${srcRoot}/views/teaching-org/${page}`))).toBe(true)
  })
})
