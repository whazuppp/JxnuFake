import { describe, expect, it } from 'vitest'
import {
  approveAvatarApplyRecord,
  createAvatarApplyRecord,
  formatApplyTime,
  resolveStudentProfile,
  validateAvatarFile
} from './avatarApply'

describe('头像更换申请工具', () => {
  it('仅允许 JPG 照片', () => {
    expect(validateAvatarFile({ type: 'image/jpeg', size: 300 * 1024 })).toEqual({
      valid: true,
      message: ''
    })

    expect(validateAvatarFile({ type: 'image/png', size: 300 * 1024 })).toEqual({
      valid: false,
      message: '请选择 JPG 格式的照片'
    })
  })

  it('兼容不同学生字段名并提供默认值', () => {
    expect(
      resolveStudentProfile({
        className: '软件工程1班',
        stuId: '2023001',
        name: '张三',
        image: '/avatar.jpg'
      })
    ).toEqual({
      className: '软件工程1班',
      studentNo: '2023001',
      studentName: '张三',
      avatarUrl: '/avatar.jpg'
    })

    expect(resolveStudentProfile({})).toEqual({
      className: '23级计算机科学与技术2班',
      studentNo: '-',
      studentName: '-',
      avatarUrl: ''
    })
  })

  it('生成申请记录并格式化时间', () => {
    const submittedAt = new Date(2024, 1, 27, 9, 51, 39)
    const record = createAvatarApplyRecord('/photo.jpg', submittedAt)

    expect(formatApplyTime(submittedAt)).toBe('2024/2/27 9:51:39')
    expect(record).toMatchObject({
      submittedAt: '2024/2/27 9:51:39',
      imageUrl: '/photo.jpg',
      status: '待审核',
      reviewedAt: '-',
      remark: ''
    })
  })

  it('可将申请记录更新为审核通过', () => {
    const record = createAvatarApplyRecord('/photo.jpg', new Date(2024, 1, 27, 9, 51, 39))
    const approvedRecords = approveAvatarApplyRecord(
      [record],
      record.id,
      new Date(2024, 1, 27, 9, 51, 44)
    )

    expect(approvedRecords[0]).toMatchObject({
      status: '审核通过',
      reviewedAt: '2024/2/27 9:51:44'
    })
  })
})
