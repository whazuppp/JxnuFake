create table student_course
(
    id          int unsigned auto_increment comment '主键ID'
        primary key,
    student_id  int unsigned  not null comment '学生ID',
    course_id   int unsigned  not null comment '课程ID',
    create_time datetime      not null comment '选课时间',
    update_time datetime      not null comment '修改时间',
    score       decimal(5, 2) null comment '课程成绩',
    semester    varchar(20)   null comment '学期',
    constraint student_id
        unique (student_id, course_id),
    constraint student_course_ibfk_1
        foreign key (student_id) references student (id)
            on update cascade on delete cascade,
    constraint student_course_ibfk_2
        foreign key (course_id) references course (id)
            on update cascade on delete cascade
)
    comment '学生选课关联表';

create index course_id
    on student_course (course_id);

