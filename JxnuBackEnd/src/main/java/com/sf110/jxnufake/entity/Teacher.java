package com.sf110.jxnufake.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Teacher {
    private Integer id;
    private String teacherNo;
    private String name;
    private Short gender;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
