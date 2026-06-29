# Jxnu Full-Stack Course System Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Preserve existing student accounts while replacing the legacy course schema and implementing the documented course, offering, enrollment, and personal timetable workflows end to end.

**Architecture:** MySQL migration scripts establish a normalized schema. Spring Boot uses entity/DTO/VO separation, transactional services, MyBatis XML mappers, JWT-derived student identity, and unified business errors. Vue 3 uses guarded routes, focused API modules, and three student-facing pages backed by pure timetable-grid helpers.

**Tech Stack:** MySQL 8, Java 17, Spring Boot 3.4, MyBatis 3, JUnit 5, Mockito, Vue 3, Vue Router 4, Element Plus, Axios, Vite 3, Vitest.

---

## File map

### Database

- Create `sql/01-migrate-schema.sql`: preserve `student`, replace teaching tables, and add constraints/triggers.
- Create `sql/02-seed-reference-data.sql`: reference data, courses, teachers, offerings, and schedules.
- Create `sql/03-seed-enrollments.sql`: idempotent enrollments for existing accounts.

### Backend foundation

- Modify `JxnuBackEnd/pom.xml`: keep Lombok and JWT/Interceptor authentication while adding validation.
- Keep `JxnuBackEnd/src/main/resources/application.properties` OSS key settings unchanged.
- Modify `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/Config/WebConfig.java`: interceptor registration.
- Modify `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/Interceptor/LoginCheckInterceptor.java`: exact login exclusion and JSON 401.
- Create `exception/BusinessException.java`, `exception/GlobalExceptionHandler.java`.
- Create `utils/CurrentStudent.java`: JWT student-ID extraction.

### Backend domain modules

- Create `entity/AdministrativeClass.java`, `ClassPeriod.java`, `Classroom.java`, `Course.java`, `CourseOffering.java`, `CourseSchedule.java`, `Semester.java`, `Student.java`, `StudentCourse.java`, `Teacher.java`.
- Create `dto/CourseRequest.java`, `TeacherRequest.java`, `OfferingRequest.java`, `ScheduleRequest.java`, `ChangePasswordRequest.java`.
- Create `vo/StudentInfoVO.java`, `OfferingVO.java`, `ScheduleVO.java`, `StudentSummaryVO.java`, `TimetableVO.java`.
- Modify/create `controller/StuController.java`, `CourseController.java`, `TeacherController.java`, `ReferenceController.java`, `OfferingController.java`, and `StudentCourseController.java`.
- Modify/create `service/StuService.java`, `CourseService.java`, `TeacherService.java`, `ReferenceService.java`, `OfferingService.java`, and `StudentCourseService.java`.
- Modify/create `service/impl/StuServiceImpl.java`, `CourseServiceImpl.java`, `TeacherServiceImpl.java`, `ReferenceServiceImpl.java`, `OfferingServiceImpl.java`, and `StudentCourseServiceImpl.java`.
- Modify/create `mapper/StuMapper.java`, `CourseMapper.java`, `TeacherMapper.java`, `ReferenceMapper.java`, `OfferingMapper.java`, and `StudentCourseMapper.java`.
- Modify/create `resources/mapper/StuMapper.xml`, `CourseMapper.xml`, `TeacherMapper.xml`, `ReferenceMapper.xml`, `OfferingMapper.xml`, and `StudentCourseMapper.xml`.

### Frontend

- Modify `src/router/index.js`, `src/utils/request.js`, `src/api/course.js`, `src/api/stu.js`.
- Create `src/api/teacher.js`, `src/api/reference.js`, `src/api/offering.js`, `src/api/studentCourse.js`.
- Create `src/utils/timetable.js`.
- Create `src/utils/offeringState.js`.
- Modify `studenthome/index.vue`, `studenthome/schedule.vue`, `studenthome/timetable.vue`, `studenthome/courseinfo.vue`.

### Documentation and tests

- Replace `JxnuBackEnd/docs/api-documentation.md` with the complete contract.
- Update `JxnuBackEnd/docs/Jxnu.apifox.openapi.json`.
- Create focused backend service/controller tests, `JxnuFrontEnd/src/utils/timetable.test.js`, and `JxnuFrontEnd/src/utils/offeringState.test.js`.

---

### Task 1: Normalize and migrate the MySQL schema

**Files:**
- Create: `sql/01-migrate-schema.sql`
- Create: `sql/02-seed-reference-data.sql`
- Create: `sql/03-seed-enrollments.sql`

- [ ] **Step 1: Write schema assertions before the migration**

Create a verification section at the bottom of `01-migrate-schema.sql` that queries `information_schema` and fails through `SIGNAL SQLSTATE '45000'` in a stored procedure unless:

```sql
SELECT COUNT(*) INTO v_student_count_before FROM student;
-- after migration:
IF (SELECT COUNT(*) FROM student) <> v_student_count_before THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'student account count changed';
END IF;
```

Also assert that `course.teacher_name`, `course.number`, and `student_course.course_id` no longer exist.

- [ ] **Step 2: Verify the assertions fail against the legacy schema**

Run only when the user authorizes a database connection:

```powershell
mysql -uroot -p jxnu < sql/01-migrate-schema.sql
```

Expected before implementation: failure because the migration objects do not exist.

- [ ] **Step 3: Implement the migration**

Create tables in this dependency order:

```text
administrative_class
teacher
course
semester
classroom
class_period
course_offering
course_schedule
student_course
```

Capture the pre-migration student count, insert a default administrative class, add nullable `student.class_id`, populate it, then change it to `NOT NULL` and add the foreign key. Drop old `student_course` before old `course`. Add all README constraints and a `BEFORE INSERT/UPDATE` trigger on `student_course` that rejects another offering of the same course for the same student.

- [ ] **Step 4: Add deterministic seed data**

Insert all 11 periods, at least two semesters with one current semester, three classes, four teachers, six courses, six classrooms, and offerings whose schedule lengths equal course weekly periods. Use stable business codes and MySQL `ON DUPLICATE KEY UPDATE` upserts.

- [ ] **Step 5: Add idempotent enrollment seeds**

Use joins on existing students and stable course/teacher codes:

```sql
INSERT INTO student_course (student_id, offering_id, score, create_time, update_time)
SELECT s.id, o.id, NULL, NOW(), NOW()
FROM student s
JOIN course_offering o
JOIN course c ON c.id = o.course_id AND c.course_code = '262516'
JOIN semester sem ON sem.id = o.semester_id AND sem.is_current = TRUE
WHERE NOT EXISTS (
  SELECT 1 FROM student_course sc
  WHERE sc.student_id = s.id AND sc.offering_id = o.id
);
```

- [ ] **Step 6: Perform static SQL verification**

Run:

```powershell
rg -n "CREATE TABLE|FOREIGN KEY|CHECK|CREATE TRIGGER|SIGNAL SQLSTATE" sql
```

Expected: every designed table, constraint category, and trigger is present.

---

### Task 2: Establish backend error, validation, identity, and logging foundations

**Files:**
- Modify: `JxnuBackEnd/pom.xml`
- Create: `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/exception/BusinessException.java`
- Create: `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/exception/GlobalExceptionHandler.java`
- Create: `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/utils/CurrentStudent.java`
- Modify: `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/Interceptor/LoginCheckInterceptor.java`
- Modify: `JxnuBackEnd/src/main/java/com/mygroup5people/jxnufake/Config/WebConfig.java`
- Test: `JxnuBackEnd/src/test/java/com/mygroup5people/jxnufake/exception/GlobalExceptionHandlerTest.java`

- [ ] **Step 1: Write failing exception-handler tests**

```java
@WebMvcTest(controllers = TestExceptionController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {
    @Test
    void businessExceptionReturnsDocumentedResult() throws Exception {
        mvc.perform(get("/test/business"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.msg").value("容量不足"));
    }
}
```

- [ ] **Step 2: Run the focused test and confirm RED**

Run:

```powershell
.\mvnw.cmd -Dtest=GlobalExceptionHandlerTest test
```

Expected: compilation failure because the exception classes do not exist.

- [ ] **Step 3: Implement minimal foundation**

`BusinessException` extends `RuntimeException`. `GlobalExceptionHandler` handles business exceptions as `Result.error(exception.getMessage())`, converts validation errors to the first field message, logs unexpected exceptions, and returns `Result.error("服务器内部错误")`.

`CurrentStudent.id(HttpServletRequest)` parses the `token` header and safely converts the `id` claim through `Number.intValue()`.

Register the interceptor for `/**` and exclude only `/stu/login`. Return HTTP 401 with UTF-8 JSON for missing/invalid tokens.

- [ ] **Step 4: Align dependencies and logging**

Keep Lombok, JWT token generation/parsing, and the interceptor authentication flow. Add:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Keep the existing OSS settings in `application.properties`. Use Lombok `@Slf4j` in controllers and services that need console-visible operation and exception logs.

- [ ] **Step 5: Run tests and confirm GREEN**

Run `.\mvnw.cmd -Dtest=GlobalExceptionHandlerTest test`.
Expected: PASS.

---

### Task 3: Refactor student account behavior around token identity

**Files:**
- Create: `entity/Student.java`
- Create: `dto/ChangePasswordRequest.java`
- Create: `vo/StudentInfoVO.java`
- Modify: existing student controller/service/mapper/XML files
- Test: `service/impl/StuServiceImplTest.java`
- Test: `controller/StuControllerTest.java`

- [ ] **Step 1: Write failing tests**

Cover:

```java
@Test
void currentInfoUsesCurrentStudentIdAndNeverReturnsPassword() {
    when(mapper.getInfoById(7)).thenReturn(new StudentInfoVO(
        7, "student", "林凯", (short) 1, null, LocalDate.of(2023, 9, 1),
        "202326202065", 2, "23级计算机科学与技术2班"));
    StudentInfoVO result = service.getInfo(7);
    assertEquals("202326202065", result.stuId());
}

@Test
void changePasswordRejectsWrongOldPassword() {
    when(mapper.getById(7)).thenReturn(studentWithPassword("123456"));
    assertThrows(BusinessException.class,
        () -> service.changePassword(7, new ChangePasswordRequest("bad", "654321")));
    verify(mapper, never()).updatePassword(anyInt(), anyString());
}

@Test
void uploadUpdatesOnlyCurrentStudent() {
    service.updateImage(7, "https://example.test/avatar.png");
    verify(mapper).updateImageById(7, "https://example.test/avatar.png");
}
```

Controller tests must call `/student/info` without an `id` query parameter.

- [ ] **Step 2: Run focused tests and confirm RED**

Run:

```powershell
.\mvnw.cmd -Dtest=StuServiceImplTest,StuControllerTest test
```

Expected: failures because current info still requires `id`.

- [ ] **Step 3: Implement account refactor**

Map `student.class_id`, join `administrative_class`, and return:

```java
public record StudentInfoVO(
    Integer id, String username, String name, Short gender, String image,
    LocalDate entrydate, String stuId, Integer classId, String className) {}
```

Keep existing login payload compatible. Make `/student/info`, upload, and password change derive ID from JWT. Reject empty files and equal old/new passwords with clear Chinese messages.

- [ ] **Step 4: Run tests and confirm GREEN**

Run the focused tests, then `.\mvnw.cmd test`.
Expected: PASS.

---

### Task 4: Implement course, teacher, and reference-data APIs

**Files:**
- Create/modify: entity, DTO, controller, service, mapper, and XML files for `Course`, `Teacher`, `Semester`, `AdministrativeClass`, `Classroom`, `ClassPeriod`
- Test: `service/impl/CourseServiceImplTest.java`
- Test: `service/impl/TeacherServiceImplTest.java`
- Test: `controller/ReferenceControllerTest.java`

- [ ] **Step 1: Write failing CRUD and validation tests**

Cover dynamic filters, missing IDs, duplicate business codes, valid types (`THEORY`, `EXPERIMENT`, `PRACTICE`), positive credit, weekly periods 2–21, and deletion while referenced.

- [ ] **Step 2: Run focused tests and confirm RED**

Run:

```powershell
.\mvnw.cmd -Dtest=CourseServiceImplTest,TeacherServiceImplTest,ReferenceControllerTest test
```

- [ ] **Step 3: Implement entities and request DTOs**

Use:

```java
public record CourseRequest(
  @NotBlank String courseCode,
  @NotBlank String name,
  @Pattern(regexp = "THEORY|EXPERIMENT|PRACTICE") String courseType,
  @DecimalMin("0.1") BigDecimal credit,
  @Min(2) @Max(21) Integer weeklyPeriods) {}
```

Teacher requests contain `teacherNo`, `name`, nullable gender, and title.

- [ ] **Step 4: Implement REST paths exactly**

Course update is `PUT /courses/{id}`. Teacher paths are `/teachers` and `/teachers/{id}`. Reference reads are `/semesters`, `/classes`, `/classrooms`, and `/class-periods`. Put every SQL statement in mapper XML.

- [ ] **Step 5: Run tests and confirm GREEN**

Run focused tests and then the full backend suite.

---

### Task 5: Implement transactional offering and schedule management

**Files:**
- Create: `entity/CourseOffering.java`, `entity/CourseSchedule.java`
- Create: `dto/OfferingRequest.java`, `dto/ScheduleRequest.java`
- Create: `vo/OfferingVO.java`, `vo/ScheduleVO.java`, `vo/StudentSummaryVO.java`
- Create: `controller/OfferingController.java`
- Create: `service/OfferingService.java`
- Create: `service/impl/OfferingServiceImpl.java`
- Create: `mapper/OfferingMapper.java`
- Create: `resources/mapper/OfferingMapper.xml`
- Test: `service/impl/OfferingServiceImplTest.java`

- [ ] **Step 1: Write one failing test per schedule rule**

Tests:

```text
rejects one-period schedule
rejects four-period schedule
accepts two-period schedule
accepts three-period schedule
rejects weekly-period total mismatch
rejects classroom overlap
rejects teacher overlap
rejects administrative-class overlap
accepts adjacent non-overlapping periods
rolls back offering when schedule insert fails
```

- [ ] **Step 2: Run tests and confirm RED**

Run `.\mvnw.cmd -Dtest=OfferingServiceImplTest test`.

- [ ] **Step 3: Implement validation in transaction order**

Within `@Transactional`:

1. Load course, teacher, class, semester, and every classroom.
2. Validate weekday 1–7 and duration 2 or 3.
3. Validate total duration equals `weeklyPeriods`.
4. Query overlap using:

```sql
existing.start_period <= #{endPeriod}
AND existing.end_period >= #{startPeriod}
```

5. Check classroom, teacher, and administrative class independently.
6. Insert/update offering and schedules.

Exclude the current offering ID during update conflict checks.

- [ ] **Step 4: Implement query, details, deletion, and roster**

`GET /offerings` supports `semesterId`, `courseName`, `teacherName`, `classId`. Delete only when no enrollment exists. Roster access requires a `student_course` row for the current student and offering.

- [ ] **Step 5: Run tests and confirm GREEN**

Run focused and full backend tests.

---

### Task 6: Implement enrollment and personal timetable

**Files:**
- Create: `entity/StudentCourse.java`
- Create: `vo/TimetableVO.java`
- Create: `controller/StudentCourseController.java`
- Create: `service/StudentCourseService.java`
- Create: `service/impl/StudentCourseServiceImpl.java`
- Create: `mapper/StudentCourseMapper.java`
- Create: `resources/mapper/StudentCourseMapper.xml`
- Test: `service/impl/StudentCourseServiceImplTest.java`
- Test: `controller/StudentCourseControllerTest.java`

- [ ] **Step 1: Write failing behavior tests**

Cover:

```text
selects available offering
rejects full offering
rejects same course through another offering
rejects duplicate offering
withdraws current student's selection
rejects withdrawal not owned by current student
returns complete timetable fields
uses student ID from token rather than request input
```

- [ ] **Step 2: Run focused tests and confirm RED**

Run:

```powershell
.\mvnw.cmd -Dtest=StudentCourseServiceImplTest,StudentCourseControllerTest test
```

- [ ] **Step 3: Implement transactional selection**

Lock or count the offering enrollment in the same transaction, compare it with capacity, check the same course through an offering join, and insert only after all checks pass.

- [ ] **Step 4: Implement documented paths**

```text
GET    /student/selections?semesterId=
POST   /student/selections/{offeringId}
DELETE /student/selections/{offeringId}
GET    /student/timetable?semesterId=
```

Assemble `TimetableVO` with student/class/semester metadata, offering details, counts, and nested schedules.

- [ ] **Step 5: Run tests and confirm GREEN**

Run focused and full backend tests.

---

### Task 7: Align the API documentation and OpenAPI artifact

**Files:**
- Modify: `JxnuBackEnd/docs/api-documentation.md`
- Modify: `JxnuBackEnd/docs/Jxnu.apifox.openapi.json`
- Modify: `JxnuBackEnd/docs/Jxnu.postman_collection.json`

- [ ] **Step 1: Write a contract path check**

Run:

```powershell
$required = @('/stu/login','/student/info','/courses','/teachers','/semesters','/classes','/classrooms','/class-periods','/offerings','/student/selections','/student/timetable')
$text = Get-Content -Raw -Encoding UTF8 '.\JxnuBackEnd\docs\api-documentation.md'
$required | ForEach-Object { if (-not $text.Contains($_)) { throw "Missing path: $_" } }
```

Expected before update: failure for the new paths.

- [ ] **Step 2: Update all three artifacts**

Document authentication, request fields, response fields, normal examples, schedule conflict errors, capacity errors, duplicate course errors, and roster permission errors. Keep paths and field names identical to controllers.

- [ ] **Step 3: Validate JSON**

Run:

```powershell
Get-Content -Raw -Encoding UTF8 '.\JxnuBackEnd\docs\Jxnu.apifox.openapi.json' | ConvertFrom-Json | Out-Null
Get-Content -Raw -Encoding UTF8 '.\JxnuBackEnd\docs\Jxnu.postman_collection.json' | ConvertFrom-Json | Out-Null
```

Expected: no error.

---

### Task 8: Fix frontend authentication, request handling, and API modules

**Files:**
- Modify: `JxnuFrontEnd/src/router/index.js`
- Modify: `JxnuFrontEnd/src/utils/request.js`
- Modify: `JxnuFrontEnd/src/api/course.js`
- Modify: `JxnuFrontEnd/src/api/stu.js`
- Create: `JxnuFrontEnd/src/api/teacher.js`
- Create: `JxnuFrontEnd/src/api/reference.js`
- Create: `JxnuFrontEnd/src/api/offering.js`
- Create: `JxnuFrontEnd/src/api/studentCourse.js`

- [ ] **Step 1: Add Vitest and write failing route/request tests**

Add `"test": "vitest run"` and Vitest as a dev dependency. Tests verify malformed local storage does not crash, protected routes redirect, login redirects away when authenticated, and 401 clears storage.

- [ ] **Step 2: Run tests and confirm RED**

Run `npm test`.
Expected: failures because guards and safe parsing are missing.

- [ ] **Step 3: Implement route metadata and guards**

Add `requiresAuth` to the main layout route, redirect `/studenthome` to `/studenthome/schedule`, and add a catch-all. Use one safe `readLoginUser()` helper.

- [ ] **Step 4: Implement robust request handling**

Attach token only when present. On 401, remove `loginUser` and route to login. Use `error.response?.status` and reject the original error. Do not show “success” styling for failures.

- [ ] **Step 5: Align every API signature**

Examples:

```js
export const updateCourseApi = (id, data) => request.put(`/courses/${id}`, data)
export const getStudentInfoApi = () => request.get('/student/info')
export const queryOfferingsApi = (params) => request.get('/offerings', { params })
export const selectOfferingApi = (id) => request.post(`/student/selections/${id}`)
```

- [ ] **Step 6: Run tests and confirm GREEN**

Run `npm test`.

---

### Task 9: Build and test the personal timetable grid

**Files:**
- Create: `JxnuFrontEnd/src/utils/timetable.js`
- Create: `JxnuFrontEnd/src/utils/timetable.test.js`
- Modify: `JxnuFrontEnd/src/views/studenthome/schedule.vue`

- [ ] **Step 1: Write failing pure-function tests**

```js
it('marks continuation cells hidden for a three-period course', () => {
  const grid = buildTimetableGrid([{ schedules: [{ weekday: 4, startPeriod: 1, endPeriod: 3 }] }])
  expect(grid[1][4].rowspan).toBe(3)
  expect(grid[2][4].hidden).toBe(true)
  expect(grid[3][4].hidden).toBe(true)
})
```

Also test empty cells, two separate schedules for one course, and malformed ranges.

- [ ] **Step 2: Run the focused test and confirm RED**

Run `npm test -- src/utils/timetable.test.js`.

- [ ] **Step 3: Implement `buildTimetableGrid`**

Return an 11-by-7 addressable structure whose start cell contains the course and `rowspan`, continuation cells contain `hidden: true`, and empty cells remain renderable.

- [ ] **Step 4: Replace the legacy CRUD page**

Load semesters, current timetable, and student info. Render seven weekday columns and eleven period rows, applying `rowspan`. Add loading/error/empty states, selected-course details, and a roster dialog using `/offerings/{id}/students`.

- [ ] **Step 5: Run tests and confirm GREEN**

Run frontend tests and `npm run build`.

---

### Task 10: Build offering selection and course catalog pages

**Files:**
- Modify: `JxnuFrontEnd/src/views/studenthome/timetable.vue`
- Modify: `JxnuFrontEnd/src/views/studenthome/courseinfo.vue`
- Modify: `JxnuFrontEnd/src/views/studenthome/index.vue`
- Create: `JxnuFrontEnd/src/utils/offeringState.js`
- Create: `JxnuFrontEnd/src/utils/offeringState.test.js`

- [ ] **Step 1: Write failing offering-state tests**

Test:

```js
it('serializes only non-empty offering filters', () => {
  expect(toOfferingParams({ semesterId: 1, courseName: '', teacherName: '吴', classId: null }))
    .toEqual({ semesterId: 1, teacherName: '吴' })
})

it('recognizes selected offering IDs', () => {
  expect(selectedOfferingIds([{ id: 8 }, { offeringId: 9 }])).toEqual(new Set([8, 9]))
})

it('prefers a backend business message', () => {
  expect(businessMessage({ msg: '课程容量已满' }, '选课失败')).toBe('课程容量已满')
})
```

- [ ] **Step 2: Run tests and confirm RED**

Run `npm test`.

- [ ] **Step 3: Implement offering selection page**

Add semester selection and filters for course name, teacher, and class. Render schedules, capacity/count, and select/withdraw actions. Refresh offerings and selections after success. Display `result.msg` on business failure.

- [ ] **Step 4: Implement course catalog**

Render course code, name, type label, credit, and weekly periods. Add filters for course code, name, and type. Remove all teacher fields and course-maintenance dialogs.

- [ ] **Step 5: Fix student shell**

Call `getStudentInfoApi()` with no ID, update avatar from the response, and ensure responsive sidebar/main overflow.

- [ ] **Step 6: Run tests and confirm GREEN**

Run `npm test` and `npm run build`.

---

### Task 11: Full verification and handoff

**Files:**
- Modify only files required by discovered test failures.

- [ ] **Step 1: Run backend verification**

```powershell
.\mvnw.cmd clean test
```

Expected: BUILD SUCCESS with all tests passing.

- [ ] **Step 2: Run frontend verification**

```powershell
npm test
npm run build
```

Expected: all tests pass and Vite build succeeds.

- [ ] **Step 3: Verify contract-to-code paths**

Search controller mappings and frontend API paths, then compare them with the required path list from Task 7. Resolve every mismatch.

- [ ] **Step 4: Verify required project conventions**

Run:

```powershell
rg -n "@Slf4j|HandlerInterceptor|JwtUtils|aliyun.oss.accessKey" JxnuBackEnd
```

Expected: JWT plus interceptor authentication, Lombok logging, and property-based OSS keys remain present.

- [ ] **Step 5: Review the final diff**

Because `D:\JxnuProject` is not currently a Git repository, use file inventories and focused searches rather than `git diff`. Confirm no unrelated views or assets were modified.

- [ ] **Step 6: Optional live integration**

Only with explicit user authorization: back up the database, run all three SQL scripts, start Spring Boot and Vite, then exercise login → offerings → select → timetable → roster → withdraw.
