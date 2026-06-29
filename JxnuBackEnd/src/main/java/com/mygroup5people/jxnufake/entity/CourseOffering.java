package com.mygroup5people.jxnufake.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseOffering {
    private Integer id;
    private Integer courseId;
    private Integer teacherId;
    private Integer classId;
    private Integer semesterId;
    private Integer capacity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
