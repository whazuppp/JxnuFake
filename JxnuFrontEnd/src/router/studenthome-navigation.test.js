import { existsSync, readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const srcRoot = fileURLToPath(new URL('../', import.meta.url))
const readSource = (path) => readFileSync(new URL(path, import.meta.url), 'utf8')

describe('学生之家导航', () => {
  it('只保留指定菜单并清理无用页面', () => {
    const menu = readSource('../views/studenthome/index.vue')
    const router = readSource('./index.js')
    const removedPages = [
      'academicrecord.vue',
      'coursefeedback.vue',
      'learningexp.vue',
      'courseinfo.vue',
      'timetable.vue',
      'shortmsg.vue',
      'classroom.vue',
      'degreeaudit.vue',
      'makeup.vue',
      'scorequery.vue',
      'questionbox.vue',
      'doublemajor.vue',
      'outschool.vue'
    ]

    expect(menu).toContain('/studenthome/teacherinfo')
    expect(menu).not.toMatch(/学籍档案|课程反馈|学习体验|课程信息|开课安排|短信平台|教室资源安排|双学位课程安排|补缓考安排|教学查询|教务意见箱|辅修双专业双学位报名|毕业生毕业学位申请/)
    expect(router).toContain("path: 'teacherinfo'")
    expect(router).not.toMatch(/AcademicRecordView|CourseFeedbackView|LearningExpView|CourseInfoView|TimetableView|ExamView|ShortMsgView|ClassroomView|DegreeAuditView|MakeupView|ScoreQueryView|QuestionBoxView|DoubleMajorView|OutSchoolView/)
    expect(existsSync(`${srcRoot}/views/studenthome/exam.vue`)).toBe(false)
    expect(removedPages.every((page) => !existsSync(`${srcRoot}/views/studenthome/${page}`))).toBe(true)
  })
})
