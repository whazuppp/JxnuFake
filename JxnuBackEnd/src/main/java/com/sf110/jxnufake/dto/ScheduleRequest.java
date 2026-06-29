package com.mygroup5people.jxnufake.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleRequest {
    @NotNull @Min(1) @Max(7)
    private Integer weekday;
    @NotNull @Min(1) @Max(11)
    private Integer startPeriod;
    @NotNull @Min(1) @Max(11)
    private Integer endPeriod;
    @NotNull
    private Integer classroomId;
}
