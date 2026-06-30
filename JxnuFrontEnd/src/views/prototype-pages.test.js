import { existsSync, readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const srcRoot = fileURLToPath(new URL('../', import.meta.url))

describe('原型图片页面', () => {
  it('辅导和建议意见页面展示对应原型', () => {
    const tutoring = readFileSync(new URL('./studycenter/tutoring.vue', import.meta.url), 'utf8')
    const opinion = readFileSync(new URL('./interaction/stuopinion.vue', import.meta.url), 'utf8')

    expect(tutoring).toContain('@/assets/tutoring-flow.png')
    expect(opinion).toContain('@/assets/stu-opinion.png')
    expect(existsSync(`${srcRoot}/assets/tutoring-flow.png`)).toBe(true)
    expect(existsSync(`${srcRoot}/assets/stu-opinion.png`)).toBe(true)
  })

  it('学生信息页面提供姓名和学号条件查询', () => {
    const studentInfo = readFileSync(new URL('./studenthome/studentinfo.vue', import.meta.url), 'utf8')

    expect(studentInfo).toContain('条件查询')
    expect(studentInfo).toContain("label: '学号'")
    expect(studentInfo).toContain("label: '精确'")
    expect(studentInfo).toContain("label: '模糊'")
  })

  it('教工信息页面提供姓名和教号条件查询', () => {
    const teacherInfo = readFileSync(new URL('./studenthome/teacherinfo.vue', import.meta.url), 'utf8')

    expect(teacherInfo).toContain('条件查询')
    expect(teacherInfo).toContain("label: '教号'")
    expect(teacherInfo).toContain("label: '精确'")
    expect(teacherInfo).toContain("label: '模糊'")
  })

  it('毕业生图像采集页面包含完整业务骨架', () => {
    const graduation = readFileSync(new URL('./studenthome/graduation.vue', import.meta.url), 'utf8')

    expect(graduation).toContain('江西师范大学毕业生图像采集信息校对表（本部）')
    expect(graduation).toContain('没有找到你的记录！')
    expect(graduation).toContain('提交')
    expect(graduation).toContain('确认不再更改')
    expect(graduation).toContain('disabled')
    expect(graduation).toContain('籍贯')
    expect(graduation).toContain('手机号')
    expect(graduation).toContain('备注')
    expect(graduation).toContain('说明')
  })

  it('期末考试安排页面展示第二学期真实课程与原型表头', () => {
    const examArrange = readFileSync(new URL('./studenthome/examarrange.vue', import.meta.url), 'utf8')

    expect(examArrange).toContain('考试安排')
    expect(examArrange).toContain('课程号')
    expect(examArrange).toContain('课程名称标识')
    expect(examArrange).toContain('考试时间')
    expect(examArrange).toContain('262516')
    expect(examArrange).toContain('Web应用技术')
    expect(examArrange).toContain('262517')
    expect(examArrange).toContain('数据库系统')
    expect(examArrange).toContain('262518')
    expect(examArrange).toContain('Java程序设计')
    expect(examArrange).toContain('exam-table')
    expect(examArrange).toContain('remark-link')
  })

  it('培养方案页面展示唯一专业选项与完整培养方案正文', () => {
    const program = readFileSync(new URL('./studenthome/program.vue', import.meta.url), 'utf8')

    expect(program).toContain('培养方案查询')
    expect(program).toContain("2023年")
    expect(program).toContain("2024年")
    expect(program).toContain("2025年")
    expect(program).toContain("2026年")
    expect(program).toContain('计算机科学与技术（综合型）')
    expect(program).toContain('培养目标')
    expect(program).toContain('规格要求')
    expect(program).toContain('方向介绍')
    expect(program).toContain('主干学科')
    expect(program).toContain('毕业最低学分')
    expect(program).toContain('170')
    expect(program).toContain('查看培养大纲')
    expect(program).toContain('programme-table')
    expect(program).toContain('toolbar-panel')
  })
})
