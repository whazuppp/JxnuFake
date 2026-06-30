# Student Teacher Directory Search Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build matching student and teacher directory pages under `学生之家/公共服务` with screenshot-style condition search, including a new student list API and consistent exact/fuzzy search behavior.

**Architecture:** Extend the backend list-query flow so `student` and `teacher` both accept a field-specific filter plus a match mode. On the frontend, add two small Vue pages that share the same query bar structure and table presentation while keeping only the field options different.

**Tech Stack:** Spring Boot 3, MyBatis XML mappers, JUnit 5 + Mockito, Vue 3 `<script setup>`, Element Plus, Vitest.

---

### Task 1: Backend Search Behavior

**Files:**
- Modify: `D:\JxnuProject\JxnuBackEnd\src\test\java\com\sf110\jxnufake\service\impl\StuServiceImplTest.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\test\java\com\sf110\jxnufake\service\impl\TeacherServiceImplTest.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\service\StuService.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\service\impl\StuServiceImpl.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\service\TeacherService.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\service\impl\TeacherServiceImpl.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\mapper\StuMapper.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\mapper\TeacherMapper.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\resources\mapper\StuMapper.xml`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\resources\mapper\TeacherMapper.xml`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\controller\StuController.java`
- Modify: `D:\JxnuProject\JxnuBackEnd\src\main\java\com\sf110\jxnufake\controller\TeacherController.java`

- [ ] **Step 1: Write the failing backend tests**

```java
@Test
void studentListPassesFilterAndExactModeToMapper() {
    Stu filter = new Stu();
    filter.setName("林凯");
    List<Stu> expected = List.of(filter);
    when(stuMapper.list("name", "林凯", true)).thenReturn(expected);

    List<Stu> actual = stuService.list("name", "林凯", true);

    assertEquals(expected, actual);
    verify(stuMapper).list("name", "林凯", true);
}

@Test
void teacherListPassesFilterAndFuzzyModeToMapper() {
    Teacher teacher = new Teacher();
    teacher.setTeacherNo("T2026");
    List<Teacher> expected = List.of(teacher);
    when(teacherMapper.list("teacherNo", "T2026", false)).thenReturn(expected);

    List<Teacher> actual = teacherService.list("teacherNo", "T2026", false);

    assertEquals(expected, actual);
    verify(teacherMapper).list("teacherNo", "T2026", false);
}
```

- [ ] **Step 2: Run backend tests to verify they fail**

Run: `.\mvnw.cmd -Dtest=StuServiceImplTest,TeacherServiceImplTest test`
Expected: FAIL because the new `list(field, keyword, exact)` signatures do not exist yet.

- [ ] **Step 3: Write minimal backend implementation**

```java
@GetMapping("/students")
public Result listStudents(@RequestParam(required = false) String field,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "true") boolean exact) {
    return Result.success(stuService.list(field, keyword, exact));
}

@GetMapping
public Result list(@RequestParam(required = false) String field,
                   @RequestParam(required = false) String keyword,
                   @RequestParam(defaultValue = "true") boolean exact) {
    return Result.success(teacherService.list(field, keyword, exact));
}
```

```java
List<Stu> list(@Param("field") String field, @Param("keyword") String keyword, @Param("exact") boolean exact);
List<Teacher> list(@Param("field") String field, @Param("keyword") String keyword, @Param("exact") boolean exact);
```

```xml
<choose>
    <when test="field == 'stuId'">
        and stu_id
        <choose>
            <when test="exact">= #{keyword}</when>
            <otherwise>like concat('%', #{keyword}, '%')</otherwise>
        </choose>
    </when>
    <otherwise>
        and name
        <choose>
            <when test="exact">= #{keyword}</when>
            <otherwise>like concat('%', #{keyword}, '%')</otherwise>
        </choose>
    </otherwise>
</choose>
```

- [ ] **Step 4: Run backend tests to verify they pass**

Run: `.\mvnw.cmd -Dtest=StuServiceImplTest,TeacherServiceImplTest test`
Expected: PASS

### Task 2: Frontend Search Pages

**Files:**
- Modify: `D:\JxnuProject\JxnuFrontEnd\src\views\prototype-pages.test.js`
- Create: `D:\JxnuProject\JxnuFrontEnd\src\api\directory.js`
- Modify: `D:\JxnuProject\JxnuFrontEnd\src\views\studenthome\studentinfo.vue`
- Modify: `D:\JxnuProject\JxnuFrontEnd\src\views\studenthome\teacherinfo.vue`

- [ ] **Step 1: Write the failing frontend tests**

```js
it('renders student directory search controls', async () => {
  const source = await fs.readFile(studentInfoPath, 'utf8')
  expect(source).toContain('条件查询')
  expect(source).toContain("label: '学号'")
  expect(source).toContain("label: '精确'")
})

it('renders teacher directory search controls', async () => {
  const source = await fs.readFile(teacherInfoPath, 'utf8')
  expect(source).toContain('条件查询')
  expect(source).toContain("label: '教号'")
  expect(source).toContain("label: '模糊'")
})
```

- [ ] **Step 2: Run frontend tests to verify they fail**

Run: `npm run test -- src/views/prototype-pages.test.js`
Expected: FAIL because both page files are still empty.

- [ ] **Step 3: Write minimal frontend implementation**

```js
export const queryStudentsApi = (params) => request.get('/students', { params })
export const queryTeachersApi = (params) => request.get('/teachers', { params })
```

```vue
const fieldOptions = [
  { label: '姓名', value: 'name' },
  { label: '学号', value: 'stuId' }
]

const matchOptions = [
  { label: '精确', value: true },
  { label: '模糊', value: false }
]
```

```vue
<el-table :data="rows">
  <el-table-column prop="name" label="姓名" />
  <el-table-column prop="stuId" label="学号" />
</el-table>
```

- [ ] **Step 4: Run frontend tests to verify they pass**

Run: `npm run test -- src/views/prototype-pages.test.js`
Expected: PASS

### Task 3: Full Verification

**Files:**
- No code changes expected

- [ ] **Step 1: Run focused backend verification**

Run: `.\mvnw.cmd -Dtest=StuServiceImplTest,TeacherServiceImplTest test`
Expected: PASS

- [ ] **Step 2: Run focused frontend verification**

Run: `npm run test -- src/views/prototype-pages.test.js`
Expected: PASS

- [ ] **Step 3: Run one broader frontend regression check**

Run: `npm run test -- src/router/studenthome-navigation.test.js`
Expected: PASS
