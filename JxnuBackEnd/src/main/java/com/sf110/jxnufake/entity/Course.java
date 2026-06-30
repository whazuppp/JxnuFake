package com.sf110.jxnufake.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Course {
    private Integer id;
    private String courseCode;
    private String name;
    private String courseType;
    private BigDecimal credit;
    private Integer weeklyPeriods;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
