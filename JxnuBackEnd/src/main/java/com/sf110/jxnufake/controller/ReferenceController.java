package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReferenceController {
    private final ReferenceService referenceService;

    @GetMapping("/semesters")
    public Result semesters() { return Result.success(referenceService.semesters()); }
    @GetMapping("/classes")
    public Result classes() { return Result.success(referenceService.classes()); }
    @GetMapping("/classrooms")
    public Result classrooms() { return Result.success(referenceService.classrooms()); }
    @GetMapping("/class-periods")
    public Result classPeriods() { return Result.success(referenceService.classPeriods()); }
}
