package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.pojo.Course;
import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result list(Course course) {
        List<Course> courses = courseService.list(course);
        log.info("Query course list: {}", course);
        return Result.success(courses);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Course course = courseService.selectById(id);
        return Result.success(course);
    }

    @PostMapping
    public Result insert(@RequestBody Course course) {
        courseService.insert(course);
        return Result.success(course);
    }

    @PutMapping
    public Result update(@RequestBody Course course) {
        courseService.update(course);
        return Result.success(course);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.delete(id);
        return Result.success();
    }
}