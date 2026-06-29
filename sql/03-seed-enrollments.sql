USE jxnu;

-- 将现有账号加入 2025-2026 学年的全部演示开课班，保留账号本身不变。
INSERT INTO student_course (student_id, offering_id, score, create_time, update_time)
SELECT s.id, o.id, NULL, NOW(), NOW()
FROM student s
CROSS JOIN course_offering o
JOIN semester sem ON sem.id = o.semester_id
WHERE sem.academic_year = '2025-2026'
  AND NOT EXISTS (
      SELECT 1
      FROM student_course sc
      WHERE sc.student_id = s.id
        AND sc.offering_id = o.id
  );
