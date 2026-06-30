USE jxnu;

SET @student_count_before = (SELECT COUNT(*) FROM student);
SET FOREIGN_KEY_CHECKS = 0;

DROP TRIGGER IF EXISTS trg_student_course_same_course_insert;
DROP TRIGGER IF EXISTS trg_student_course_same_course_update;
DROP TABLE IF EXISTS student_course;
DROP TABLE IF EXISTS course_schedule;
DROP TABLE IF EXISTS course_offering;
DROP TABLE IF EXISTS class_period;
DROP TABLE IF EXISTS classroom;
DROP TABLE IF EXISTS semester;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS administrative_class;

CREATE TABLE administrative_class (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    class_code VARCHAR(30) NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_administrative_class_code UNIQUE (class_code)
) COMMENT '行政班';

INSERT INTO administrative_class (class_code, class_name)
VALUES ('DEFAULT', '未分配行政班');

SET @has_class_id = (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'student'
      AND column_name = 'class_id'
);
SET @add_class_id_sql = IF(
    @has_class_id = 0,
    'ALTER TABLE student ADD COLUMN class_id INT UNSIGNED NULL COMMENT ''行政班ID'' AFTER stu_id',
    'SELECT 1'
);
PREPARE add_class_id_stmt FROM @add_class_id_sql;
EXECUTE add_class_id_stmt;
DEALLOCATE PREPARE add_class_id_stmt;

UPDATE student
SET class_id = (SELECT id FROM administrative_class WHERE class_code = 'DEFAULT')
WHERE class_id IS NULL;

ALTER TABLE student
    MODIFY class_id INT UNSIGNED NOT NULL COMMENT '行政班ID',
    ADD CONSTRAINT fk_student_class
        FOREIGN KEY (class_id) REFERENCES administrative_class (id)
        ON UPDATE RESTRICT ON DELETE RESTRICT;

CREATE TABLE teacher (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    teacher_no VARCHAR(30) NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender TINYINT UNSIGNED NULL,
    title VARCHAR(50) NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_teacher_no UNIQUE (teacher_no),
    CONSTRAINT chk_teacher_gender CHECK (gender IS NULL OR gender IN (1, 2))
) COMMENT '教师';

CREATE TABLE course (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    course_type VARCHAR(20) NOT NULL,
    credit DECIMAL(3, 1) NOT NULL,
    weekly_periods TINYINT UNSIGNED NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_course_code UNIQUE (course_code),
    CONSTRAINT chk_course_type CHECK (course_type IN ('THEORY', 'EXPERIMENT', 'PRACTICE')),
    CONSTRAINT chk_course_credit CHECK (credit > 0),
    CONSTRAINT chk_course_weekly_periods CHECK (weekly_periods BETWEEN 2 AND 21)
) COMMENT '课程';

CREATE TABLE semester (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    academic_year VARCHAR(20) NOT NULL,
    term_no TINYINT UNSIGNED NOT NULL,
    name VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_semester_year_term UNIQUE (academic_year, term_no),
    CONSTRAINT chk_semester_term CHECK (term_no IN (1, 2, 3)),
    CONSTRAINT chk_semester_dates CHECK (end_date >= start_date)
) COMMENT '学期';

CREATE TABLE classroom (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    building VARCHAR(100) NOT NULL,
    room_no VARCHAR(30) NOT NULL,
    capacity INT UNSIGNED NOT NULL,
    CONSTRAINT uk_classroom_building_room UNIQUE (building, room_no),
    CONSTRAINT chk_classroom_capacity CHECK (capacity > 0)
) COMMENT '教室';

CREATE TABLE class_period (
    period_no TINYINT UNSIGNED PRIMARY KEY,
    day_part VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT chk_class_period_no CHECK (period_no BETWEEN 1 AND 11),
    CONSTRAINT chk_class_period_time CHECK (end_time > start_time)
) COMMENT '节次';

CREATE TABLE course_offering (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    course_id INT UNSIGNED NOT NULL,
    teacher_id INT UNSIGNED NOT NULL,
    class_id INT UNSIGNED NOT NULL,
    semester_id INT UNSIGNED NOT NULL,
    capacity INT UNSIGNED NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_offering UNIQUE (course_id, teacher_id, class_id, semester_id),
    CONSTRAINT chk_offering_capacity CHECK (capacity > 0),
    CONSTRAINT fk_offering_course FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT fk_offering_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    CONSTRAINT fk_offering_class FOREIGN KEY (class_id) REFERENCES administrative_class (id),
    CONSTRAINT fk_offering_semester FOREIGN KEY (semester_id) REFERENCES semester (id)
) COMMENT '开课班';

CREATE TABLE course_schedule (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    offering_id INT UNSIGNED NOT NULL,
    weekday TINYINT UNSIGNED NOT NULL,
    start_period TINYINT UNSIGNED NOT NULL,
    end_period TINYINT UNSIGNED NOT NULL,
    classroom_id INT UNSIGNED NOT NULL,
    CONSTRAINT uk_schedule UNIQUE (offering_id, weekday, start_period, end_period),
    CONSTRAINT chk_schedule_weekday CHECK (weekday BETWEEN 1 AND 7),
    CONSTRAINT chk_schedule_length CHECK (end_period - start_period + 1 IN (2, 3)),
    CONSTRAINT fk_schedule_offering FOREIGN KEY (offering_id) REFERENCES course_offering (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_schedule_start_period FOREIGN KEY (start_period) REFERENCES class_period (period_no),
    CONSTRAINT fk_schedule_end_period FOREIGN KEY (end_period) REFERENCES class_period (period_no),
    CONSTRAINT fk_schedule_classroom FOREIGN KEY (classroom_id) REFERENCES classroom (id)
) COMMENT '排课时段';

CREATE INDEX idx_schedule_conflict
    ON course_schedule (weekday, start_period, end_period, classroom_id);

CREATE TABLE student_course (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    student_id INT UNSIGNED NOT NULL,
    offering_id INT UNSIGNED NOT NULL,
    score DECIMAL(5, 2) NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_student_offering UNIQUE (student_id, offering_id),
    CONSTRAINT chk_student_course_score CHECK (score IS NULL OR score BETWEEN 0 AND 100),
    CONSTRAINT fk_student_course_student FOREIGN KEY (student_id) REFERENCES student (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_student_course_offering FOREIGN KEY (offering_id) REFERENCES course_offering (id)
        ON DELETE RESTRICT
) COMMENT '学生选课';

CREATE INDEX idx_student_course_offering ON student_course (offering_id);

DELIMITER $$

CREATE TRIGGER trg_student_course_same_course_insert
BEFORE INSERT ON student_course
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM student_course sc
        JOIN course_offering selected_offering ON selected_offering.id = sc.offering_id
        JOIN course_offering new_offering ON new_offering.id = NEW.offering_id
        WHERE sc.student_id = NEW.student_id
          AND selected_offering.course_id = new_offering.course_id
          AND selected_offering.semester_id = new_offering.semester_id
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '同一学生不能重复选择同一学期内的同一课程';
    END IF;
END$$

CREATE TRIGGER trg_student_course_same_course_update
BEFORE UPDATE ON student_course
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM student_course sc
        JOIN course_offering selected_offering ON selected_offering.id = sc.offering_id
        JOIN course_offering new_offering ON new_offering.id = NEW.offering_id
        WHERE sc.student_id = NEW.student_id
          AND sc.id <> OLD.id
          AND selected_offering.course_id = new_offering.course_id
          AND selected_offering.semester_id = new_offering.semester_id
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '同一学生不能重复选择同一学期内的同一课程';
    END IF;
END$$

DELIMITER ;

SET FOREIGN_KEY_CHECKS = 1;

DROP PROCEDURE IF EXISTS verify_jxnu_migration;
DELIMITER $$
CREATE PROCEDURE verify_jxnu_migration()
BEGIN
    IF (SELECT COUNT(*) FROM student) <> @student_count_before THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '学生账号数量发生变化';
    END IF;
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'course'
          AND column_name IN ('teacher_name', 'number')
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '旧课程字段仍然存在';
    END IF;
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'student_course'
          AND column_name = 'course_id'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '旧选课字段仍然存在';
    END IF;
END$$
DELIMITER ;
CALL verify_jxnu_migration();
DROP PROCEDURE verify_jxnu_migration;
