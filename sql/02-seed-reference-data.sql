USE jxnu;

-- 本脚本只重建 2025-2026 学年的演示开课数据，保留学生账号及其他学年数据。
DELETE sc
FROM student_course sc
JOIN course_offering o ON o.id = sc.offering_id
JOIN semester sem ON sem.id = o.semester_id
WHERE sem.academic_year = '2025-2026';

DELETE cs
FROM course_schedule cs
JOIN course_offering o ON o.id = cs.offering_id
JOIN semester sem ON sem.id = o.semester_id
WHERE sem.academic_year = '2025-2026';

DELETE o
FROM course_offering o
JOIN semester sem ON sem.id = o.semester_id
WHERE sem.academic_year = '2025-2026';

INSERT INTO administrative_class (class_code, class_name)
VALUES
    ('CS2023-01', '23级计算机科学与技术1班'),
    ('CS2023-02', '23级计算机科学与技术2班'),
    ('SE2023-01', '23级软件工程1班'),
    ('HHH-01', '教工黄梅轩1班'),
    ('ZK-01', '合班张凯.1班'),
    ('YT-01', '教工杨拓1班'),
    ('ZJ-01', '教工.郑娟1班'),
    ('CBL-01', '程柏良1班')
ON DUPLICATE KEY UPDATE class_name = VALUES(class_name);

UPDATE student
SET class_id = (SELECT id FROM administrative_class WHERE class_code = 'CS2023-02')
WHERE class_id = (SELECT id FROM administrative_class WHERE class_code = 'DEFAULT');

INSERT INTO teacher (teacher_no, name, gender, title)
VALUES
    ('T-HM', '黄梅', 2, '教授'),
    ('T-ZK', '张凯', 1, '教授'),
    ('T-YT', '杨拓', 1, '副教授'),
    ('T-ZJ', '郑娟', 2, '副教授'),
    ('T-CBL', '程柏良', 1, '讲师'),
    ('T-ZJG', '曾纪国', 1, '副教授'),
    ('T-WSS', '吴水秀', 2, '讲师'),
    ('T-ZW', '周伟', 1, '副教授'),
    ('T-LM', '刘敏', 2, '讲师'),
    ('T-CQ', '陈强', 1, '教授'),
    ('T-WJ', '王静', 2, '讲师')
ON DUPLICATE KEY UPDATE
    name = VALUES(name), gender = VALUES(gender), title = VALUES(title);

INSERT INTO course (course_code, name, course_type, credit, weekly_periods)
VALUES
    ('002926', '西汉海昏侯墓与海昏侯刘贺研究', 'THEORY', 2.0, 2),
    ('028024', '习近平新时代中国特色社会主义思想概论', 'THEORY', 3.0, 3),
    ('251080', '中国现代文学', 'THEORY', 2.0, 2),
    ('259051', '中国行政史', 'THEORY', 2.0, 2),
    ('262391', '人工智能基础', 'THEORY', 2.0, 2),
    ('262412', '软件工程导论（理论）', 'THEORY', 2.0, 2),
    ('262496', '软件工程导论（实验）', 'EXPERIMENT', 1.0, 2),
    ('262516', 'Web应用技术', 'PRACTICE', 3.0, 4),
    ('DL2025', '深度学习', 'THEORY', 3.0, 3),
    ('CPP2025', 'C++设计', 'PRACTICE', 4.0, 4),
    ('COMP2025', '编译原理', 'THEORY', 4.0, 4),
    ('WEB2025', 'Web程序设计', 'PRACTICE', 4.0, 4)
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
    ('惟义楼', 'W3205', 100),
    ('惟义楼', 'W5201', 100),
    ('惟义楼', 'W5401', 100),
    ('惟义楼', 'W7208', 100),
    ('惟义楼', 'W3409', 100),
    ('惟义楼', 'W7206', 100),
    ('先骕楼', 'X4313g', 80),
    ('先骕楼', 'X4301', 80),
    ('实验大楼', 'S301', 60),
    ('实验大楼', 'S302', 60)
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

-- 开课班：课程号、教师号、班级代码、学期号、容量。
INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-HM'
JOIN administrative_class ac ON ac.class_code = 'HHH-01'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '002926';

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 100
FROM course c
JOIN teacher t ON t.teacher_no = 'T-ZK'
JOIN administrative_class ac ON ac.class_code = 'ZK-01'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '028024';

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-YT'
JOIN administrative_class ac ON ac.class_code = 'YT-01'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '251080';

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-ZJ'
JOIN administrative_class ac ON ac.class_code = 'ZJ-01'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '259051';

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-CBL'
JOIN administrative_class ac ON ac.class_code = 'CBL-01'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '262391';

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-ZJG'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code IN ('262412', '262496');

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = 'T-WSS'
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 2
WHERE c.course_code = '262516';

INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, 60
FROM course c
JOIN teacher t ON t.teacher_no = CASE c.course_code
    WHEN 'DL2025' THEN 'T-ZW'
    WHEN 'CPP2025' THEN 'T-LM'
    WHEN 'COMP2025' THEN 'T-CQ'
    WHEN 'WEB2025' THEN 'T-WJ'
END
JOIN administrative_class ac ON ac.class_code = 'CS2023-02'
JOIN semester sem ON sem.academic_year = '2025-2026' AND sem.term_no = 1
WHERE c.course_code IN ('DL2025', 'CPP2025', 'COMP2025', 'WEB2025');

-- 本学期排课。
INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, seed.weekday, seed.start_period, seed.end_period, cr.id
FROM (
    SELECT '002926' course_code, 3 weekday, 6 start_period, 7 end_period, 'W5201' room_no
    UNION ALL SELECT '028024', 2, 3, 5, 'W5401'
    UNION ALL SELECT '251080', 4, 6, 7, 'W7208'
    UNION ALL SELECT '259051', 4, 10, 11, 'W3409'
    UNION ALL SELECT '262391', 7, 6, 7, 'W7206'
    UNION ALL SELECT '262412', 1, 6, 7, 'W3205'
    UNION ALL SELECT '262496', 1, 8, 9, 'X4313g'
    UNION ALL SELECT '262516', 4, 1, 2, 'W3205'
    UNION ALL SELECT '262516', 4, 3, 4, 'X4313g'
) seed
JOIN course c ON c.course_code = seed.course_code
JOIN course_offering o ON o.course_id = c.id
JOIN semester sem ON sem.id = o.semester_id
    AND sem.academic_year = '2025-2026' AND sem.term_no = 2
JOIN classroom cr ON cr.room_no = seed.room_no;

-- 上学期排课。
INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, seed.weekday, seed.start_period, seed.end_period, cr.id
FROM (
    SELECT 'DL2025' course_code, 1 weekday, 1 start_period, 3 end_period, 'W7206' room_no
    UNION ALL SELECT 'CPP2025', 2, 1, 2, 'X4301'
    UNION ALL SELECT 'CPP2025', 4, 6, 7, 'S301'
    UNION ALL SELECT 'COMP2025', 3, 1, 2, 'W5201'
    UNION ALL SELECT 'COMP2025', 5, 3, 4, 'W5201'
    UNION ALL SELECT 'WEB2025', 1, 6, 7, 'W3205'
    UNION ALL SELECT 'WEB2025', 3, 8, 9, 'S302'
) seed
JOIN course c ON c.course_code = seed.course_code
JOIN course_offering o ON o.course_id = c.id
JOIN semester sem ON sem.id = o.semester_id
    AND sem.academic_year = '2025-2026' AND sem.term_no = 1
JOIN classroom cr ON cr.room_no = seed.room_no;
