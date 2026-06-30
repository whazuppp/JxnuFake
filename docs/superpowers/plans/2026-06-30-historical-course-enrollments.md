# Historical Course Enrollments Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make all four students enrolled in the same 11 first-term courses and three second-term courses, with working roster links.

**Architecture:** Rebuild the 2025-2026 seed as the source of truth, enroll every student into every offering for both terms, and render the API timetable directly so roster requests use numeric offering IDs. Keep the existing timetable presentation unchanged.

**Tech Stack:** MySQL 8, Spring Boot/MyBatis, Vue 3, Vitest

---

### Task 1: Add regression tests

**Files:**
- Modify: `JxnuFrontEnd/src/utils/sqlSeed.test.js`
- Modify: `JxnuFrontEnd/src/utils/semesterSchedule.test.js`

- [ ] Assert the historical seed contains all 11 first-term course codes and enrolls all students into all 2025-2026 offerings.
- [ ] Assert `coursesForSemester` returns API courses with numeric offering IDs for the first term.
- [ ] Run `npm test -- --run src/utils/sqlSeed.test.js src/utils/semesterSchedule.test.js` and verify the new assertions fail for the existing seed and fixed history IDs.

### Task 2: Rebuild historical seed data

**Files:**
- Modify: `sql/02-seed-reference-data.sql`
- Modify: `sql/03-seed-enrollments.sql`

- [ ] Replace first-term demo offerings with the 11 courses, teachers, classes, rooms, and schedules represented by the prototype.
- [ ] Keep exactly three second-term offerings.
- [ ] Make the enrollment seed insert every existing student into every 2025-2026 offering using `NOT EXISTS`.
- [ ] Run the focused SQL tests and verify they pass.

### Task 3: Use real offering IDs in the timetable

**Files:**
- Modify: `JxnuFrontEnd/src/utils/semesterSchedule.js`

- [ ] Remove fixed first-term substitution and return API timetable courses for every semester.
- [ ] Run the focused frontend tests and verify they pass.

### Task 4: Apply and verify

**Files:**
- No production file changes.

- [ ] Execute `sql/02-seed-reference-data.sql` followed by `sql/03-seed-enrollments.sql` against local MySQL.
- [ ] Query enrollment counts and verify each of four students has 11 first-term and three second-term selections.
- [ ] Query roster counts and verify every historical offering contains four students.
- [ ] Run all backend tests, all frontend tests, and the frontend production build.
- [ ] Call a live roster endpoint with a logged-in student and verify it returns four records.
