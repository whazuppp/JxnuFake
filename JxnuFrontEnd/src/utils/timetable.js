export function buildTimetableGrid(offerings = []) {
  const grid = Array.from({ length: 11 }, (_, row) =>
    Array.from({ length: 7 }, (_, column) => ({
      period: row + 1,
      weekday: column + 1,
      hidden: false,
      rowspan: 1,
      offering: null
    }))
  )
  for (const offering of offerings) {
    for (const schedule of offering.schedules || []) {
      const { weekday, startPeriod, endPeriod } = schedule
      if (weekday < 1 || weekday > 7 || startPeriod < 1 || endPeriod > 11 || endPeriod < startPeriod) continue
      const start = grid[startPeriod - 1][weekday - 1]
      start.offering = { ...offering, activeSchedule: schedule }
      start.rowspan = endPeriod - startPeriod + 1
      for (let period = startPeriod + 1; period <= endPeriod; period++) {
        grid[period - 1][weekday - 1].hidden = true
      }
    }
  }
  return grid
}

export function normalizeCourseRows(offerings = []) {
  const unique = new Map()
  offerings.forEach(offering => {
    if (offering?.id != null && !unique.has(offering.id)) unique.set(offering.id, offering)
  })
  return [...unique.values()].sort((left, right) =>
    String(left.courseCode || '').localeCompare(String(right.courseCode || ''))
  )
}
