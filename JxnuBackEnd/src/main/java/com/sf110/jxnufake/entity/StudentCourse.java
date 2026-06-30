package com.sf110.jxnufake.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StudentCourse {
    private Integer id;
    private Integer studentId;
    private Integer offeringId;
    private BigDecimal score;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
