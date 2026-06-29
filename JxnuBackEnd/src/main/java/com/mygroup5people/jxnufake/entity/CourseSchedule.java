package com.mygroup5people.jxnufake.entity;

import lombok.Data;

@Data
public class CourseSchedule {
    private Integer id;
    private Integer offeringId;
    private Integer weekday;
    private Integer startPeriod;
    private Integer endPeriod;
    private Integer classroomId;
}
