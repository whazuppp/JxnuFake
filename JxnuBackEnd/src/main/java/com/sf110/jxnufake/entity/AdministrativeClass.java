package com.sf110.jxnufake.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdministrativeClass {
    private Integer id;
    private String classCode;
    private String className;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
