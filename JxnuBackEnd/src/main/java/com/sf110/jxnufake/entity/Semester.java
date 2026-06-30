package com.sf110.jxnufake.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Semester {
    private Integer id;
    private String academicYear;
    private Integer termNo;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean current;
}
