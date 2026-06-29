package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.dto.CourseRequest;
import com.mygroup5people.jxnufake.entity.Course;
import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public Result list(@RequestParam(required = false) String courseCode,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String courseType) {
        Course filter = new Course();
        filter.setCourseCode(courseCode);
        filter.setName(name);
        filter.setCourseType(courseType);
        return Result.success(courseService.list(filter));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        return Result.success(courseService.selectById(id));
    }

    @PostMapping
    public Result create(@Valid @RequestBody CourseRequest request) {
        log.info("新增课程: {}", request.getCourseCode());
        return Result.success(courseService.insert(request));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @Valid @RequestBody CourseRequest request) {
        log.info("修改课程: {}", id);
        return Result.success(courseService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.delete(id);
        return Result.success();
    }
}
