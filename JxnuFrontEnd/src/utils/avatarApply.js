const AVATAR_APPLY_STORAGE_KEY = 'studentAvatarApplyRecords'
export const STUDENT_AVATAR_UPDATED_EVENT = 'student-avatar-updated'

export function validateAvatarFile(file, { acceptTypes = ['image/jpeg', 'image/jpg'] } = {}) {
  const isAcceptedType = acceptTypes.includes(file.type)
  if (!isAcceptedType) {
    return { valid: false, message: '请选择 JPG 格式的照片' }
  }

  return { valid: true, message: '' }
}

export function resolveStudentProfile(student = {}) {
  return {
    className: student.className || student.class || '23级计算机科学与技术2班',
    studentNo: student.studentNo || student.stuId || student.studentId || '-',
    studentName: student.studentName || student.name || '-',
    avatarUrl: student.image || student.avatar || student.avatarUrl || ''
  }
}

export function createAvatarApplyRecord(imageUrl, submittedAt = new Date()) {
  return {
    id: submittedAt.getTime(),
    submittedAt: formatApplyTime(submittedAt),
    imageUrl,
    status: '待审核',
    reviewedAt: '-',
    remark: ''
  }
}

export function formatApplyTime(date) {
  const value = date instanceof Date ? date : new Date(date)
  const year = value.getFullYear()
  const month = value.getMonth() + 1
  const day = value.getDate()
  const hours = value.getHours()
  const minutes = value.getMinutes()
  const seconds = value.getSeconds()

  return `${year}/${month}/${day} ${hours}:${padTime(minutes)}:${padTime(seconds)}`
}

function padTime(value) {
  return String(value).padStart(2, '0')
}

export function readAvatarApplyRecords() {
  try {
    return JSON.parse(localStorage.getItem(AVATAR_APPLY_STORAGE_KEY)) || []
  } catch {
    localStorage.removeItem(AVATAR_APPLY_STORAGE_KEY)
    return []
  }
}

export function saveAvatarApplyRecords(records) {
  localStorage.setItem(AVATAR_APPLY_STORAGE_KEY, JSON.stringify(records))
}

export function approveAvatarApplyRecord(records, recordId, reviewedAt = new Date()) {
  return records.map((record) =>
    record.id === recordId
      ? {
          ...record,
          status: '审核通过',
          reviewedAt: formatApplyTime(reviewedAt)
        }
      : record
  )
}
