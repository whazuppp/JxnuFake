package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.mapper.CourseMapper;
import com.mygroup5people.jxnufake.pojo.Course;
import com.mygroup5people.jxnufake.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> list(Course course) {
        return courseMapper.list(course);
    }

    @Override
    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public void insert(Course course) {
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.insert(course);
    }

    @Override
    public void update(Course course) {
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.update(course);
    }

    @Override
    public void delete(Integer id) {
        courseMapper.delete(id);
    }
}