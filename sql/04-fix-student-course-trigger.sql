USE jxnu;

DROP TRIGGER IF EXISTS trg_student_course_same_course_insert;
DROP TRIGGER IF EXISTS trg_student_course_same_course_update;

DELIMITER $$

CREATE TRIGGER trg_student_course_same_course_insert
BEFORE INSERT ON student_course
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM student_course sc
        JOIN course_offering selected_offering
            ON selected_offering.id = sc.offering_id
        JOIN course_offering new_offering
            ON new_offering.id = NEW.offering_id
        WHERE sc.student_id = NEW.student_id
          AND selected_offering.course_id = new_offering.course_id
          AND selected_offering.semester_id = new_offering.semester_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '同一学生同一学期不能重复选择同一课程';
    END IF;
END$$

CREATE TRIGGER trg_student_course_same_course_update
BEFORE UPDATE ON student_course
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM student_course sc
        JOIN course_offering selected_offering
            ON selected_offering.id = sc.offering_id
        JOIN course_offering new_offering
            ON new_offering.id = NEW.offering_id
        WHERE sc.student_id = NEW.student_id
          AND sc.id <> OLD.id
          AND selected_offering.course_id = new_offering.course_id
          AND selected_offering.semester_id = new_offering.semester_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '同一学生同一学期不能重复选择同一课程';
    END IF;
END$$

DELIMITER ;
