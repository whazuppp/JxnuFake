export const toOfferingParams = (filters) =>
  Object.fromEntries(Object.entries(filters).filter(([, value]) => value !== '' && value !== null && value !== undefined))

export const selectedOfferingIds = (items = []) =>
  new Set(
    items
      .map(item => item.offeringId ?? item.id)
      .filter(value => value !== '' && value !== null && value !== undefined)
  )

export const toSelectionRows = (offerings = [], selections = []) => {
  const selectedIds = selectedOfferingIds(selections)
  return offerings.map(offering => {
    const capacity = Number(offering.capacity ?? 0)
    const studentCount = Number(offering.studentCount ?? 0)
    return {
      ...offering,
      selected: selectedIds.has(offering.id),
      remaining: Math.max(capacity - studentCount, 0)
    }
  })
}

export const businessMessage = (result, fallback) =>
  result?.msg || result?.response?.data?.msg || result?.message || fallback
