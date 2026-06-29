package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.pojo.Course;

import java.util.List;

public interface CourseService {
    List<Course> list(Course course);

    Course selectById(Integer id);

    void insert(Course course);

    void update(Course course);

    void delete(Integer id);
}