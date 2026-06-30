package com.sf110.jxnufake.controller;

import com.sf110.jxnufake.dto.TeacherRequest;
import com.sf110.jxnufake.entity.Teacher;
import com.sf110.jxnufake.pojo.Result;
import com.sf110.jxnufake.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public Result list(@RequestParam(required = false) String field,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "true") boolean exact) {
        return Result.success(teacherService.list(field, keyword, exact));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        return Result.success(teacherService.get(id));
    }

    @PostMapping
    public Result create(@Valid @RequestBody TeacherRequest request) {
        return Result.success(teacherService.create(request));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @Valid @RequestBody TeacherRequest request) {
        return Result.success(teacherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        teacherService.delete(id);
        return Result.success();
    }
}
