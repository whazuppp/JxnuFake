USE jxnu;

INSERT INTO student_course (student_id, offering_id, score, create_time, update_time)
SELECT s.id, o.id, NULL, NOW(), NOW()
FROM student s
JOIN course_offering o
JOIN course c ON c.id = o.course_id AND c.course_code = '262516'
JOIN semester sem ON sem.id = o.semester_id AND sem.is_current = TRUE
WHERE NOT EXISTS (
    SELECT 1
    FROM student_course sc
    WHERE sc.student_id = s.id
      AND sc.offering_id = o.id
);
