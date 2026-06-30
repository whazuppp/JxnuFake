USE jxnu;

-- 重建未来学期演示选课数据；脚本会清空该学期已有选课。
INSERT INTO semester (academic_year, term_no, name, start_date, end_date, is_current)
VALUES ('2026-2027', 1, '2026-2027学年第1学期', '2026-09-01', '2027-01-17', FALSE)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date);

INSERT INTO administrative_class (class_code, class_name)
VALUES
    ('CS2023-01', '23级计算机科学与技术1班'),
    ('CS2023-02', '23级计算机科学与技术2班')
ON DUPLICATE KEY UPDATE class_name = VALUES(class_name);

INSERT INTO teacher (teacher_no, name, gender, title)
VALUES
    ('T-NEXT-WEB', '吴老师', NULL, '教师'),
    ('T-NEXT-DB', '张老师', NULL, '教师'),
    ('T-NEXT-JAVA', '陈老师', NULL, '教师')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    gender = VALUES(gender),
    title = VALUES(title);

INSERT INTO course (course_code, name, course_type, credit, weekly_periods)
VALUES
    ('262516', 'Web应用技术', 'THEORY', 3.0, 3),
    ('262517', '数据库系统', 'THEORY', 4.0, 4),
    ('262518', 'Java程序设计', 'PRACTICE', 3.0, 3)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    course_type = VALUES(course_type),
    credit = VALUES(credit),
    weekly_periods = VALUES(weekly_periods);

INSERT INTO classroom (building, room_no, capacity)
VALUES
    ('惟义楼', 'W2201', 80),
    ('惟义楼', 'W2501', 100),
    ('先骕楼', 'X4313g', 80),
    ('实验大楼', 'S301', 60),
    ('实验大楼', 'S302', 60)
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

DELETE selected
FROM student_course selected
JOIN course_offering offering ON offering.id = selected.offering_id
JOIN semester target_sem ON target_sem.id = offering.semester_id
WHERE target_sem.academic_year = '2026-2027'
  AND target_sem.term_no = 1;

DELETE schedule
FROM course_schedule schedule
JOIN course_offering offering ON offering.id = schedule.offering_id
JOIN semester target_sem ON target_sem.id = offering.semester_id
WHERE target_sem.academic_year = '2026-2027'
  AND target_sem.term_no = 1;

DELETE offering
FROM course_offering offering
JOIN semester target_sem ON target_sem.id = offering.semester_id
WHERE target_sem.academic_year = '2026-2027'
  AND target_sem.term_no = 1;

-- 三门课程分别为计科1班、计科2班创建开课班。
INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT
    course.id,
    teacher.id,
    class.id,
    target_sem.id,
    seed.capacity
FROM (
    SELECT '262516' course_code, 'CS2023-01' class_code, 'T-NEXT-WEB' teacher_no, 60 capacity
    UNION ALL SELECT '262516', 'CS2023-02', 'T-NEXT-WEB', 60
    UNION ALL SELECT '262517', 'CS2023-01', 'T-NEXT-DB', 60
    UNION ALL SELECT '262517', 'CS2023-02', 'T-NEXT-DB', 60
    UNION ALL SELECT '262518', 'CS2023-01', 'T-NEXT-JAVA', 50
    UNION ALL SELECT '262518', 'CS2023-02', 'T-NEXT-JAVA', 50
) seed
JOIN course course ON course.course_code = seed.course_code
JOIN teacher teacher ON teacher.teacher_no = seed.teacher_no
JOIN administrative_class class ON class.class_code = seed.class_code
JOIN semester target_sem
    ON target_sem.academic_year = '2026-2027'
    AND target_sem.term_no = 1;

-- 同一课程的两个班级使用不同时间和教室。
INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT
    offering.id,
    seed.weekday,
    seed.start_period,
    seed.end_period,
    classroom.id
FROM (
    SELECT '262516' course_code, 'CS2023-01' class_code, 1 weekday, 1 start_period, 3 end_period, 'W2201' room_no
    UNION ALL SELECT '262516', 'CS2023-02', 4, 1, 3, 'X4313g'
    UNION ALL SELECT '262517', 'CS2023-01', 2, 3, 4, 'W2201'
    UNION ALL SELECT '262517', 'CS2023-01', 5, 6, 7, 'W2201'
    UNION ALL SELECT '262517', 'CS2023-02', 1, 3, 4, 'W2501'
    UNION ALL SELECT '262517', 'CS2023-02', 4, 6, 7, 'W2501'
    UNION ALL SELECT '262518', 'CS2023-01', 2, 6, 8, 'S301'
    UNION ALL SELECT '262518', 'CS2023-02', 3, 6, 8, 'S302'
) seed
JOIN course course ON course.course_code = seed.course_code
JOIN administrative_class class ON class.class_code = seed.class_code
JOIN semester target_sem
    ON target_sem.academic_year = '2026-2027'
    AND target_sem.term_no = 1
JOIN course_offering offering
    ON offering.course_id = course.id
    AND offering.class_id = class.id
    AND offering.semester_id = target_sem.id
JOIN classroom classroom ON classroom.room_no = seed.room_no;

SELECT
    course.course_code,
    course.name AS course_name,
    class.class_name,
    GROUP_CONCAT(
        CONCAT(schedule.weekday, ':', schedule.start_period, '-', schedule.end_period, '@', classroom.building, classroom.room_no)
        ORDER BY schedule.weekday, schedule.start_period
        SEPARATOR ';'
    ) AS schedule
FROM course_offering offering
JOIN semester target_sem ON target_sem.id = offering.semester_id
JOIN course course ON course.id = offering.course_id
JOIN administrative_class class ON class.id = offering.class_id
LEFT JOIN course_schedule schedule ON schedule.offering_id = offering.id
LEFT JOIN classroom classroom ON classroom.id = schedule.classroom_id
WHERE target_sem.academic_year = '2026-2027'
  AND target_sem.term_no = 1
GROUP BY offering.id, course.course_code, course.name, class.class_name
ORDER BY course.course_code, class.class_name;
