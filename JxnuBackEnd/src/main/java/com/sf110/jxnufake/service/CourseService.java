package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.dto.CourseRequest;
import com.mygroup5people.jxnufake.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> list(Course course);

    Course selectById(Integer id);

    Course insert(CourseRequest request);

    Course update(Integer id, CourseRequest request);

    void delete(Integer id);
}
