package com.sf110.jxnufake.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeacherRequest {
    @NotBlank(message = "教师工号不能为空")
    private String teacherNo;
    @NotBlank(message = "教师姓名不能为空")
    private String name;
    @Min(value = 1, message = "教师性别不正确")
    @Max(value = 2, message = "教师性别不正确")
    private Short gender;
    private String title;
}
