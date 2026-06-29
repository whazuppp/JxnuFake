package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.StudentCourseService;
import com.mygroup5people.jxnufake.utils.CurrentStudent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentCourseController {
    private final StudentCourseService service;

    @GetMapping("/selections")
    public Result selections(@RequestParam Integer semesterId, HttpServletRequest request) {
        return Result.success(service.list(CurrentStudent.id(request), semesterId));
    }

    @PostMapping("/selections/{offeringId}")
    public Result select(@PathVariable Integer offeringId, HttpServletRequest request) {
        service.select(CurrentStudent.id(request), offeringId);
        return Result.success();
    }

    @DeleteMapping("/selections/{offeringId}")
    public Result withdraw(@PathVariable Integer offeringId, HttpServletRequest request) {
        service.withdraw(CurrentStudent.id(request), offeringId);
        return Result.success();
    }

    @GetMapping("/timetable")
    public Result timetable(@RequestParam Integer semesterId, HttpServletRequest request) {
        return Result.success(service.timetable(CurrentStudent.id(request), semesterId));
    }
}
