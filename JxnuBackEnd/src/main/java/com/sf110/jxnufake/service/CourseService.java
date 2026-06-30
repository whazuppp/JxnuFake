package com.sf110.jxnufake.service;

import com.sf110.jxnufake.dto.CourseRequest;
import com.sf110.jxnufake.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> list(Course course);

    Course selectById(Integer id);

    Course insert(CourseRequest request);

    Course update(Integer id, CourseRequest request);

    void delete(Integer id);
}
