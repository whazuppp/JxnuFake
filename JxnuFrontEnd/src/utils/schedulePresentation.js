export const DEFAULT_AVATAR =
  'data:image/svg+xml;charset=UTF-8,' +
  encodeURIComponent(
    '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 180 240">' +
    '<rect width="180" height="240" fill="#e8eef4"/>' +
    '<circle cx="90" cy="82" r="42" fill="#aebdca"/>' +
    '<path d="M24 224c6-57 35-87 66-87s60 30 66 87" fill="#aebdca"/>' +
    '</svg>'
  )

export function resolveAvatar(image) {
  return typeof image === 'string' && image.trim() ? image : DEFAULT_AVATAR
}

export function toRosterCards(students = []) {
  return students.map((student, index) => ({
    index: index + 1,
    studentNo: student.studentNo,
    name: student.name,
    className: student.className,
    image: resolveAvatar(student.image)
  }))
}
