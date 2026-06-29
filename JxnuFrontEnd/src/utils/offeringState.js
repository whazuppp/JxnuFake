export const toOfferingParams = (filters) =>
  Object.fromEntries(Object.entries(filters).filter(([, value]) => value !== '' && value !== null && value !== undefined))

export const selectedOfferingIds = (items = []) =>
  new Set(items.map(item => item.offeringId ?? item.id))

export const businessMessage = (result, fallback) => result?.msg || fallback
