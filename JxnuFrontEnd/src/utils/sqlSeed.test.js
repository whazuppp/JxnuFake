import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const schemaMigration = readFileSync(
  fileURLToPath(new URL('../../../sql/01-migrate-schema.sql', import.meta.url)),
  'utf8'
)
const referenceSeed = readFileSync(
  fileURLToPath(new URL('../../../sql/02-seed-reference-data.sql', import.meta.url)),
  'utf8'
)
const enrollmentSeed = readFileSync(
  fileURLToPath(new URL('../../../sql/03-seed-enrollments.sql', import.meta.url)),
  'utf8'
)

describe('课程表种子数据', () => {
  it('包含本学期原型中的八门课程', () => {
    const courses = [
      '西汉海昏侯墓与海昏侯刘贺研究',
      '习近平新时代中国特色社会主义思想概论',
      '中国现代文学',
      '中国行政史',
      '人工智能基础',
      '软件工程导论（理论）',
      '软件工程导论（实验）',
      'Web应用技术'
    ]

    courses.forEach(course => expect(referenceSeed).toContain(course))
  })

  it('包含上学期四门课程及指定周课时', () => {
    expect(referenceSeed).toContain("('DL2025', '深度学习', 'THEORY', 3.0, 3)")
    expect(referenceSeed).toContain("('CPP2025', 'C++设计', 'PRACTICE', 4.0, 4)")
    expect(referenceSeed).toContain("('COMP2025', '编译原理', 'THEORY', 4.0, 4)")
    expect(referenceSeed).toContain("('WEB2025', 'Web程序设计', 'PRACTICE', 4.0, 4)")
  })

  it('为两个学期的全部种子开课班建立选课关系', () => {
    expect(enrollmentSeed).toContain("sem.academic_year = '2025-2026'")
    expect(enrollmentSeed).not.toContain("c.course_code = '262516'")
  })

  it('只限制同一学期内重复选择同一课程，避免第一学期选课被其他学期拦截', () => {
    expect(schemaMigration).toContain('selected_offering.semester_id = new_offering.semester_id')
  })
})
