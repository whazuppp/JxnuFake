package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.mapper.CourseMapper;
import com.mygroup5people.jxnufake.dto.CourseRequest;
import com.mygroup5people.jxnufake.entity.Course;
import com.mygroup5people.jxnufake.exception.BusinessException;
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
    public Course insert(CourseRequest request) {
        Course course = fromRequest(request);
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.insert(course);
        return course;
    }

    @Override
    public Course update(Integer id, CourseRequest request) {
        if (courseMapper.selectById(id) == null) {
            throw new BusinessException("课程不存在");
        }
        Course course = fromRequest(request);
        course.setId(id);
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.update(course);
        return courseMapper.selectById(id);
    }

    @Override
    public void delete(Integer id) {
        if (courseMapper.countOfferings(id) > 0) {
            throw new BusinessException("课程已被开课班引用，不能删除");
        }
        courseMapper.delete(id);
    }

    private Course fromRequest(CourseRequest request) {
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setName(request.getName());
        course.setCourseType(request.getCourseType());
        course.setCredit(request.getCredit());
        course.setWeeklyPeriods(request.getWeeklyPeriods());
        return course;
    }
}
