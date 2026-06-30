package com.sf110.jxnufake.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    @NotBlank(message = "课程号不能为空")
    private String courseCode;
    @NotBlank(message = "课程名称不能为空")
    private String name;
    @Pattern(regexp = "THEORY|EXPERIMENT|PRACTICE", message = "课程类型不正确")
    private String courseType;
    @NotNull(message = "学分不能为空")
    @DecimalMin(value = "0.1", message = "学分必须大于0")
    private BigDecimal credit;
    @NotNull(message = "每周课时不能为空")
    @Min(value = 2, message = "每周课时不能少于2")
    @Max(value = 21, message = "每周课时不能大于21")
    private Integer weeklyPeriods;
}
