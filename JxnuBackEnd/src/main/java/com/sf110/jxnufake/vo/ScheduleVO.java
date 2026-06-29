package com.mygroup5people.jxnufake.vo;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ScheduleVO {
    private Integer id;
    private Integer weekday;
    private Integer startPeriod;
    private Integer endPeriod;
    private Integer classroomId;
    private String building;
    private String roomNo;
    private LocalTime startTime;
    private LocalTime endTime;
}
