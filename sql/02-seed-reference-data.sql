USE jxnu;

INSERT INTO administrative_class (class_code, class_name)
VALUES
    ('CS2023-01', '23级计算机科学与技术1班'),
    ('CS2023-02', '23级计算机科学与技术2班'),
    ('SE2023-01', '23级软件工程1班')
ON DUPLICATE KEY UPDATE class_name = VALUES(class_name);

UPDATE student
SET class_id = (SELECT id FROM administrative_class WHERE class_code = 'CS2023-02')
WHERE class_id = (SELECT id FROM administrative_class WHERE class_code = 'DEFAULT');

INSERT INTO teacher (teacher_no, name, gender, title)
VALUES
    ('T001', '张老师', 1, '教授'),
    ('T002', '李老师', 2, '副教授'),
    ('T003', '吴水秀', 2, '讲师'),
    ('T004', '陈老师', 1, '讲师')
ON DUPLICATE KEY UPDATE name = VALUES(name), gender = VALUES(gender), title = VALUES(title);

INSERT INTO course (course_code, name, course_type, credit, weekly_periods)
VALUES
    ('262516', 'Web应用技术', 'THEORY', 3.0, 3),
    ('262517', '数据库原理', 'THEORY', 4.0, 4),
    ('262518', 'Java程序设计', 'PRACTICE', 3.0, 3),
    ('262519', '计算机网络', 'THEORY', 4.0, 4),
    ('262520', '软件工程', 'THEORY', 2.0, 2),
    ('262521', '操作系统实验', 'EXPERIMENT', 2.0, 2)
ON DUPLICATE KEY UPDATE
    name = VALUES(name), course_type = VALUES(course_type),
    credit = VALUES(credit), weekly_periods = VALUES(weekly_periods);

INSERT INTO semester (academic_year, term_no, name, start_date, end_date, is_current)
VALUES
    ('2025-2026', 1, '2025-2026学年第1学期', '2025-09-01', '2026-01-18', FALSE),
    ('2025-2026', 2, '2025-2026学年第2学期', '2026-02-23', '2026-07-05', TRUE)
ON DUPLICATE KEY UPDATE
    name = VALUES(name), start_date = VALUES(start_date),
    end_date = VALUES(end_date), is_current = VALUES(is_current);

INSERT INTO classroom (building, room_no, capacity)
VALUES
    ('先骕楼', 'X4313g', 80),
    ('先骕楼', 'X4301', 60),
    ('惟义楼', 'W2201', 100),
    ('惟义楼', 'W2302', 80),
    ('实验大楼', 'S301', 50),
    ('实验大楼', 'S302', 50)
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
    day_part = VALUES(day_part), start_time = VALUES(start_time), end_time = VALUES(end_time);

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T003'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '262516'
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T001'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '262517'
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 50
FROM course c
JOIN teacher t ON t.teacher_no = 'T004'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '262518'
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 4, 1, 3, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id AND c.course_code = '262516'
JOIN semester sem ON sem.id = o.semester_id AND sem.is_current = TRUE
JOIN classroom cr ON cr.building = '先骕楼' AND cr.room_no = 'X4313g'
ON DUPLICATE KEY UPDATE classroom_id = VALUES(classroom_id);

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 2, 3, 4, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id AND c.course_code = '262517'
JOIN semester sem ON sem.id = o.semester_id AND sem.is_current = TRUE
JOIN classroom cr ON cr.building = '惟义楼' AND cr.room_no = 'W2201'
ON DUPLICATE KEY UPDATE classroom_id = VALUES(classroom_id);

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 5, 6, 7, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id AND c.course_code = '262517'
JOIN semester sem ON sem.id = o.semester_id AND sem.is_current = TRUE
JOIN classroom cr ON cr.building = '惟义楼' AND cr.room_no = 'W2201'
ON DUPLICATE KEY UPDATE classroom_id = VALUES(classroom_id);

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, 3, 6, 8, cr.id
FROM course_offering o
JOIN course c ON c.id = o.course_id AND c.course_code = '262518'
JOIN semester sem ON sem.id = o.semester_id AND sem.is_current = TRUE
JOIN classroom cr ON cr.building = '实验大楼' AND cr.room_no = 'S301'
ON DUPLICATE KEY UPDATE classroom_id = VALUES(classroom_id);
