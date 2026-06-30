package com.sf110.jxnufake.entity;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ClassPeriod {
    private Integer periodNo;
    private String dayPart;
    private LocalTime startTime;
    private LocalTime endTime;
}
