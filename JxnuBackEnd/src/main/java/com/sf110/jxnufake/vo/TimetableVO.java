package com.mygroup5people.jxnufake.vo;

import lombok.Data;
import java.util.List;

@Data
public class TimetableVO {
    private Integer studentId;
    private String studentNo;
    private String studentName;
    private String className;
    private Integer semesterId;
    private String semesterName;
    private List<OfferingVO> courses;
}
