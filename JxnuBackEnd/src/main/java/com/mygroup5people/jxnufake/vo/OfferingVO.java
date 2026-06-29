package com.mygroup5people.jxnufake.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OfferingVO {
    private Integer id;
    private Integer courseId;
    private String courseCode;
    private String courseName;
    private String courseType;
    private BigDecimal credit;
    private Integer weeklyPeriods;
    private Integer teacherId;
    private String teacherNo;
    private String teacherName;
    private Integer classId;
    private String className;
    private Integer semesterId;
    private String semesterName;
    private Integer capacity;
    private Integer studentCount;
    private List<ScheduleVO> schedules;
}
