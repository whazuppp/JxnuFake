create table course
(
    id           int unsigned auto_increment comment '主键ID'
        primary key,
    name         varchar(100) not null comment '课程名称',
    number       int unsigned not null comment '课时数',
    teacher_name varchar(50)  not null comment '授课老师',
    create_time  datetime     not null comment '创建时间',
    update_time  datetime     not null comment '修改时间'
)
    comment '课程管理表';

