export function readLoginUser() {
  try {
    return JSON.parse(localStorage.getItem('loginUser')) || null
  } catch {
    localStorage.removeItem('loginUser')
    return null
  }
}
