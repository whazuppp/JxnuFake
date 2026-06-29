package com.mygroup5people.jxnufake.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OfferingRequest {
    @NotNull private Integer courseId;
    @NotNull private Integer teacherId;
    @NotNull private Integer classId;
    @NotNull private Integer semesterId;
    @NotNull @Min(1) private Integer capacity;
    @Valid @NotEmpty private List<ScheduleRequest> schedules;
}
