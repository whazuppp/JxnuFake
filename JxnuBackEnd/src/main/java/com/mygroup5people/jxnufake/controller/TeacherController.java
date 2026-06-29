package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.dto.TeacherRequest;
import com.mygroup5people.jxnufake.entity.Teacher;
import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.TeacherService;
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
    public Result list(@RequestParam(required = false) String teacherNo,
                       @RequestParam(required = false) String name) {
        Teacher filter = new Teacher();
        filter.setTeacherNo(teacherNo);
        filter.setName(name);
        return Result.success(teacherService.list(filter));
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
