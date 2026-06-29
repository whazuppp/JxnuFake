package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.dto.OfferingRequest;
import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.OfferingService;
import com.mygroup5people.jxnufake.utils.CurrentStudent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/offerings")
@RequiredArgsConstructor
public class OfferingController {
    private final OfferingService offeringService;

    @GetMapping
    public Result list(@RequestParam(required = false) Integer semesterId,
                       @RequestParam(required = false) String courseName,
                       @RequestParam(required = false) String teacherName,
                       @RequestParam(required = false) Integer classId) {
        return Result.success(offeringService.list(semesterId, courseName, teacherName, classId));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) { return Result.success(offeringService.get(id)); }

    @PostMapping
    public Result create(@Valid @RequestBody OfferingRequest request) {
        return Result.success(offeringService.create(request));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @Valid @RequestBody OfferingRequest request) {
        return Result.success(offeringService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        offeringService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/students")
    public Result students(@PathVariable Integer id, HttpServletRequest request) {
        return Result.success(offeringService.listStudents(id, CurrentStudent.id(request)));
    }
}
