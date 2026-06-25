create table student
(
    id          int unsigned auto_increment comment '主键ID'
        primary key,
    username    varchar(50)                   not null comment '用户名',
    password    varchar(100) default '123456' not null comment '密码',
    name        varchar(50)                   not null comment '姓名',
    gender      tinyint unsigned              not null comment '性别，1：男，2：女',
    image       varchar(300)                  null comment '头像路径',
    entrydate   date                          null comment '入学时间',
    stu_id      varchar(100)                  not null comment '学号',
    create_time datetime                      not null comment '创建时间',
    update_time datetime                      not null comment '修改时间',
    constraint stu_id
        unique (stu_id),
    constraint username
        unique (username)
)
    comment '学生管理表';

