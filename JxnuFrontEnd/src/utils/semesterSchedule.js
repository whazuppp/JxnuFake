export const NEXT_ACADEMIC_YEAR = '2026-2027'
export const NEXT_TERM_NO = 1

export const isSemester = (semester, academicYear, termNo) => {
  const year = semester?.academicYear ?? semester?.academic_year
  const term = semester?.termNo ?? semester?.term_no
  return year === academicYear && Number(term) === Number(termNo)
}

export const selectNextSemester = (semesters = []) =>
  semesters.filter(item => isSemester(item, NEXT_ACADEMIC_YEAR, NEXT_TERM_NO))

export const coursesForSemester = (_semester, remoteCourses = []) => remoteCourses
