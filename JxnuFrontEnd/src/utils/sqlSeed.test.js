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
const nextSemesterSeed = readFileSync(
  fileURLToPath(new URL('../../../sql/06-seed-2026-first-semester.sql', import.meta.url)),
  'utf8'
)

describe('课程表种子数据', () => {
  it('第二学期保留三门课程', () => {
    const courses = ['Web应用技术', '数据库系统', 'Java程序设计']

    courses.forEach(course => expect(referenceSeed).toContain(course))
    expect(referenceSeed).toContain(
      "UNION ALL SELECT '262518', 'T-CLS', 'CS2023-02', 2, 50"
    )
  })

  it('包含第一学期原型中的十一门课程', () => {
    const courseCodes = [
      '002174', '004040', '251176', '262021', '262092', '262275',
      '262276', '262366', '262367', '262371', '262410'
    ]

    courseCodes.forEach(code => expect(referenceSeed).toContain(`('${code}',`))
    expect(referenceSeed).toMatch(
      /sem\.academic_year = '2025-2026'\s+AND sem\.term_no = seed\.term_no/
    )
  })

  it('为所有学生建立两个学期全部开课班的选课关系', () => {
    expect(enrollmentSeed).toMatch(/from student s\s+cross join course_offering o/i)
    expect(enrollmentSeed).toContain("sem.academic_year = '2025-2026'")
    expect(enrollmentSeed).toContain('sc.student_id = s.id')
    expect(enrollmentSeed).toContain('sc.offering_id = o.id')
  })

  it('只限制同一学期内重复选择同一课程，避免第一学期选课被其他学期拦截', () => {
    expect(schemaMigration).toContain('selected_offering.semester_id = new_offering.semester_id')
  })

  it('新增2026-2027学年第1学期及其待选开课班', () => {
    expect(referenceSeed).toContain("('2026-2027', 1, '2026-2027学年第1学期'")
    expect(nextSemesterSeed).toContain("target_sem.academic_year = '2026-2027'")
    expect(nextSemesterSeed).toMatch(/insert into course_offering/i)
    expect(nextSemesterSeed).toMatch(/insert into course_schedule/i)
  })

  it('未来学期种子不预选任何学生课程', () => {
    expect(nextSemesterSeed).not.toMatch(/insert\s+into\s+student_course/i)
  })

  it('未来学期三门课程均按计科1班和2班开课', () => {
    expect(nextSemesterSeed).toContain("SELECT '262516' course_code, 'CS2023-01' class_code")
    expect(nextSemesterSeed).toContain("SELECT '262516', 'CS2023-02'")
    expect(nextSemesterSeed).toContain("SELECT '262517', 'CS2023-01'")
    expect(nextSemesterSeed).toContain("SELECT '262517', 'CS2023-02'")
    expect(nextSemesterSeed).toContain("SELECT '262518', 'CS2023-01'")
    expect(nextSemesterSeed).toContain("SELECT '262518', 'CS2023-02'")
  })

  it('同课程的两个班级使用不同排课', () => {
    expect(nextSemesterSeed).toContain(
      "SELECT '262516' course_code, 'CS2023-01' class_code, 1 weekday, 1 start_period, 3 end_period, 'W2201'"
    )
    expect(nextSemesterSeed).toContain(
      "SELECT '262516', 'CS2023-02', 4, 1, 3, 'X4313g'"
    )
    expect(nextSemesterSeed).toContain(
      "SELECT '262518', 'CS2023-01', 2, 6, 8, 'S301'"
    )
    expect(nextSemesterSeed).toContain(
      "SELECT '262518', 'CS2023-02', 3, 6, 8, 'S302'"
    )
  })
})
