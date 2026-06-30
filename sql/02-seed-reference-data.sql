USE jxnu;

-- 重建2025-2026学年演示课程，保留学生账号和其他学年数据。
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
    ('CS2023-02', '23级计算机科学与技术2班'),
    ('FM-01', '教工冯梅#1班'),
    ('YY-01', '袁艳#1班'),
    ('LY-02', '教工雷要#2班'),
    ('LJX-01', '卢家兴#1班'),
    ('LJM-01', '教工刘建明#.1班'),
    ('LWB-02', '教工.罗文兵#2班'),
    ('ZXQ-01', '教工曾雪强#1班'),
    ('LMC-01', '教工李明楚#1班')
ON DUPLICATE KEY UPDATE class_name = VALUES(class_name);

UPDATE student
SET class_id = (SELECT id FROM administrative_class WHERE class_code = 'CS2023-02')
WHERE class_id IS NULL
   OR class_id = (SELECT id FROM administrative_class WHERE class_code = 'DEFAULT');

INSERT INTO teacher (teacher_no, name, gender, title)
VALUES
    ('T-FM', '冯梅', 2, '教师'),
    ('T-YY', '袁艳', 2, '教师'),
    ('T-LY', '雷要', 1, '教师'),
    ('T-LJX', '卢家兴', 1, '教师'),
    ('T-LJM', '刘建明', 1, '教师'),
    ('T-MML', '马明磊', 1, '教师'),
    ('T-LWB', '罗文兵', 1, '教师'),
    ('T-YQH', '杨庆红', 1, '教师'),
    ('T-ZXQ', '曾雪强', 1, '教师'),
    ('T-LMC', '李明楚', 1, '教师'),
    ('T-WLS', '吴老师', NULL, '教师'),
    ('T-ZLS', '张老师', NULL, '教师'),
    ('T-CLS', '陈老师', NULL, '教师')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    gender = VALUES(gender),
    title = VALUES(title);

INSERT INTO course (course_code, name, course_type, credit, weekly_periods)
VALUES
    ('002174', '电影教育与审美专题', 'THEORY', 2.0, 2),
    ('004040', '大学生身体活动与健康', 'THEORY', 2.0, 2),
    ('251176', '儿童文学（2分）', 'THEORY', 2.0, 2),
    ('262021', 'Web程序设计', 'PRACTICE', 4.0, 4),
    ('262092', 'C++程序设计（3分）', 'PRACTICE', 3.0, 4),
    ('262275', '数据库系统（理论）', 'THEORY', 4.0, 4),
    ('262276', '数据库系统（实验）', 'EXPERIMENT', 2.0, 2),
    ('262366', '编译原理及技术（理论）', 'THEORY', 4.0, 4),
    ('262367', '编译原理及技术（实验）', 'EXPERIMENT', 2.0, 2),
    ('262371', 'Python深度学习', 'PRACTICE', 4.0, 4),
    ('262410', '网络安全', 'THEORY', 4.0, 4),
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
    ('2025-2026', 2, '2025-2026学年第2学期', '2026-02-23', '2026-07-05', TRUE),
    ('2026-2027', 1, '2026-2027学年第1学期', '2026-09-01', '2027-01-17', FALSE)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    is_current = VALUES(is_current);

INSERT INTO classroom (building, room_no, capacity)
VALUES
    ('惟义楼', 'W3210', 100),
    ('惟义楼', 'W3309', 100),
    ('惟义楼', 'W2409', 100),
    ('惟义楼', 'W2301', 100),
    ('先骕楼', 'X4313e', 80),
    ('惟义楼', 'W5204', 100),
    ('先骕楼', 'X4313a', 80),
    ('惟义楼', 'W2501', 100),
    ('先骕楼', 'X4313g', 80),
    ('惟义楼', 'W1103', 100),
    ('惟义楼', 'W1105', 100),
    ('先骕楼', 'X2407a', 80),
    ('惟义楼', 'W3103', 100),
    ('先骕楼', 'X4605a2', 80),
    ('惟义楼', 'W2201', 100),
    ('实验大楼', 'S301', 60)
ON DUPLICATE KEY UPDATE
    building = VALUES(building),
    capacity = VALUES(capacity);

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

-- 第一学期11个开课班，第二学期保留3个开课班。
INSERT INTO course_offering (course_id, teacher_id, class_id, semester_id, capacity)
SELECT c.id, t.id, ac.id, sem.id, seed.capacity
FROM (
    SELECT '002174' course_code, 'T-FM' teacher_no, 'FM-01' class_code, 1 term_no, 60 capacity
    UNION ALL SELECT '004040', 'T-YY', 'YY-01', 1, 60
    UNION ALL SELECT '251176', 'T-LY', 'LY-02', 1, 60
    UNION ALL SELECT '262021', 'T-LJX', 'LJX-01', 1, 60
    UNION ALL SELECT '262092', 'T-LJM', 'LJM-01', 1, 60
    UNION ALL SELECT '262275', 'T-MML', 'CS2023-02', 1, 60
    UNION ALL SELECT '262276', 'T-LWB', 'LWB-02', 1, 60
    UNION ALL SELECT '262366', 'T-YQH', 'CS2023-02', 1, 60
    UNION ALL SELECT '262367', 'T-YQH', 'CS2023-02', 1, 60
    UNION ALL SELECT '262371', 'T-ZXQ', 'ZXQ-01', 1, 60
    UNION ALL SELECT '262410', 'T-LMC', 'LMC-01', 1, 60
    UNION ALL SELECT '262516', 'T-WLS', 'CS2023-02', 2, 60
    UNION ALL SELECT '262517', 'T-ZLS', 'CS2023-02', 2, 60
    UNION ALL SELECT '262518', 'T-CLS', 'CS2023-02', 2, 50
) seed
JOIN course c ON c.course_code = seed.course_code
JOIN teacher t ON t.teacher_no = seed.teacher_no
JOIN administrative_class ac ON ac.class_code = seed.class_code
JOIN semester sem ON sem.academic_year = '2025-2026'
    AND sem.term_no = seed.term_no;

INSERT INTO course_schedule (offering_id, weekday, start_period, end_period, classroom_id)
SELECT o.id, seed.weekday, seed.start_period, seed.end_period, cr.id
FROM (
    SELECT '002174' course_code, 1 term_no, 5 weekday, 6 start_period, 7 end_period, 'W3210' room_no
    UNION ALL SELECT '004040', 1, 5, 8, 9, 'W3309'
    UNION ALL SELECT '251176', 1, 2, 3, 4, 'W2409'
    UNION ALL SELECT '262021', 1, 5, 1, 2, 'W2301'
    UNION ALL SELECT '262021', 1, 5, 3, 4, 'X4313e'
    UNION ALL SELECT '262092', 1, 4, 6, 7, 'W5204'
    UNION ALL SELECT '262092', 1, 4, 8, 9, 'X4313a'
    UNION ALL SELECT '262275', 1, 1, 4, 5, 'W2501'
    UNION ALL SELECT '262275', 1, 3, 6, 7, 'W2501'
    UNION ALL SELECT '262276', 1, 3, 8, 9, 'X4313g'
    UNION ALL SELECT '262366', 1, 3, 1, 2, 'W1103'
    UNION ALL SELECT '262366', 1, 4, 1, 2, 'W1105'
    UNION ALL SELECT '262367', 1, 3, 3, 4, 'X4313g'
    UNION ALL SELECT '262371', 1, 7, 6, 7, 'X2407a'
    UNION ALL SELECT '262371', 1, 7, 8, 9, 'X2407a'
    UNION ALL SELECT '262410', 1, 1, 6, 7, 'W3103'
    UNION ALL SELECT '262410', 1, 1, 8, 9, 'X4605a2'
    UNION ALL SELECT '262516', 2, 4, 1, 3, 'X4313g'
    UNION ALL SELECT '262517', 2, 2, 3, 4, 'W2201'
    UNION ALL SELECT '262517', 2, 5, 6, 7, 'W2201'
    UNION ALL SELECT '262518', 2, 3, 6, 8, 'S301'
) seed
JOIN course c ON c.course_code = seed.course_code
JOIN course_offering o ON o.course_id = c.id
JOIN semester sem ON sem.id = o.semester_id
    AND sem.academic_year = '2025-2026'
    AND sem.term_no = seed.term_no
JOIN classroom cr ON cr.room_no = seed.room_no;
