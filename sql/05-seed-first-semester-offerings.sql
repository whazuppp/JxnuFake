USE jxnu;

-- 05-seed-first-semester-offerings.sql
-- 作用：给 2025-2026 学年第1学期补充可选开课班。
-- 注意：本脚本只补“可选课程/排课”，不会给学生预选课程。
-- 如果你想让页面显示“未选/选课”按钮，不要再运行 03-seed-enrollments.sql。

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 确保基础字典数据存在
INSERT INTO administrative_class (class_code, class_name)
VALUES ('CS2023-02', '23级计算机科学与技术2班')
ON DUPLICATE KEY UPDATE class_name = VALUES(class_name);

INSERT INTO teacher (teacher_no, name, gender, title)
VALUES
    ('T-WLS', '吴老师', NULL, '教师'),
    ('T-ZLS', '张老师', NULL, '教师'),
    ('T-CLS', '陈老师', NULL, '教师')
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

INSERT INTO semester (academic_year, term_no, name, start_date, end_date, is_current)
VALUES
    ('2025-2026', 1, '2025-2026学年第1学期', '2025-09-01', '2026-01-18', FALSE),
    ('2025-2026', 2, '2025-2026学年第2学期', '2026-02-23', '2026-07-05', TRUE)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    is_current = VALUES(is_current);

INSERT INTO classroom (building, room_no, capacity)
VALUES
    ('先骕楼', 'X4313g', 80),
    ('惟义楼', 'W2201', 80),
    ('实验大楼', 'S301', 60)
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

INSERT INTO class_period (period_no, day_part, start_time, end_time)
VALUES
    (1, '上午', '08:00', '08:40'),
    (2, '上午', '08:50', '09:30'),
    (3, '上午', '09:40', '10:20'),
    (4, '上午', '10:30', '11:10'),
    (5, '上午', '11:20', '12:00'),
    (6, '下午', '14:00', '14:40'),
    (7, '下午', '14:50', '15:30'),
    (8, '下午', '15:40', '16:20'),
    (9, '下午', '16:30', '17:10'),
    (10, '晚上', '19:00', '19:40'),
    (11, '晚上', '19:50', '20:30')
ON DUPLICATE KEY UPDATE
    day_part = VALUES(day_part),
    start_time = VALUES(start_time),
    end_time = VALUES(end_time);

-- 2. 给第一学期插入开课班
INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-WLS'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 1
WHERE c.course_code = '262516'
  AND NOT EXISTS (
      SELECT 1
      FROM course_offering exist_o
      WHERE exist_o.course_id = c.id
        AND exist_o.teacher_id = t.id
        AND exist_o.class_id = ac.id
        AND exist_o.semester_id = sem.id
  );

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-ZLS'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 1
WHERE c.course_code = '262517'
  AND NOT EXISTS (
      SELECT 1
      FROM course_offering exist_o
      WHERE exist_o.course_id = c.id
        AND exist_o.teacher_id = t.id
        AND exist_o.class_id = ac.id
        AND exist_o.semester_id = sem.id
  );

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 50
FROM course c
JOIN teacher t ON t.teacher_no = 'T-CLS'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 1
WHERE c.course_code = '262518'
  AND NOT EXISTS (
      SELECT 1
      FROM course_offering exist_o
      WHERE exist_o.course_id = c.id
        AND exist_o.teacher_id = t.id
        AND exist_o.class_id = ac.id
        AND exist_o.semester_id = sem.id
  );

-- 3. 给第一学期开课班插入排课
INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 4, 1, 3, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id
JOIN semester sem ON sem.id = o.semester_id
JOIN classroom cr ON cr.building = '先骕楼' AND cr.room_no = 'X4313g'
WHERE c.course_code = '262516'
  AND sem.academic_year = '2025-2026'
  AND sem.term_no = 1
  AND NOT EXISTS (
      SELECT 1
      FROM course_schedule cs
      WHERE cs.offering_id = o.id
        AND cs.weekday = 4
        AND cs.start_period = 1
        AND cs.end_period = 3
  );

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 2, 3, 4, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id
JOIN semester sem ON sem.id = o.semester_id
JOIN classroom cr ON cr.building = '惟义楼' AND cr.room_no = 'W2201'
WHERE c.course_code = '262517'
  AND sem.academic_year = '2025-2026'
  AND sem.term_no = 1
  AND NOT EXISTS (
      SELECT 1
      FROM course_schedule cs
      WHERE cs.offering_id = o.id
        AND cs.weekday = 2
        AND cs.start_period = 3
        AND cs.end_period = 4
  );

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 5, 6, 7, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id
JOIN semester sem ON sem.id = o.semester_id
JOIN classroom cr ON cr.building = '惟义楼' AND cr.room_no = 'W2201'
WHERE c.course_code = '262517'
  AND sem.academic_year = '2025-2026'
  AND sem.term_no = 1
  AND NOT EXISTS (
      SELECT 1
      FROM course_schedule cs
      WHERE cs.offering_id = o.id
        AND cs.weekday = 5
        AND cs.start_period = 6
        AND cs.end_period = 7
  );

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 3, 6, 8, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id
JOIN semester sem ON sem.id = o.semester_id
JOIN classroom cr ON cr.building = '实验大楼' AND cr.room_no = 'S301'
WHERE c.course_code = '262518'
  AND sem.academic_year = '2025-2026'
  AND sem.term_no = 1
  AND NOT EXISTS (
      SELECT 1
      FROM course_schedule cs
      WHERE cs.offering_id = o.id
        AND cs.weekday = 3
        AND cs.start_period = 6
        AND cs.end_period = 8
  );

SET FOREIGN_KEY_CHECKS = 1;

-- 4. 验证第一学期是否已有开课班
SELECT
    sem.name AS semester_name,
    COUNT(o.id) AS offering_count
FROM semester sem
LEFT JOIN course_offering o ON o.semester_id = sem.id
WHERE sem.academic_year = '2025-2026'
GROUP BY sem.id, sem.name
ORDER BY sem.term_no;

-- 5. 查看第一学期具体课程
SELECT
    sem.name AS semester_name,
    c.course_code,
    c.name AS course_name,
    t.name AS teacher_name,
    ac.class_name,
    o.capacity,
    COUNT(sc.id) AS selected_count
FROM course_offering o
JOIN semester sem ON sem.id = o.semester_id
JOIN course c ON c.id = o.course_id
JOIN teacher t ON t.id = o.teacher_id
JOIN administrative_class ac ON ac.id = o.class_id
LEFT JOIN student_course sc ON sc.offering_id = o.id
WHERE sem.academic_year = '2025-2026'
  AND sem.term_no = 1
GROUP BY sem.name, c.course_code, c.name, t.name, ac.class_name, o.capacity
ORDER BY c.course_code;
