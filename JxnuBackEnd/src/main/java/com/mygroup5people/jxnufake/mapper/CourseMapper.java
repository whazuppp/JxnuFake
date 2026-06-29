package com.mygroup5people.jxnufake.mapper;

import com.mygroup5people.jxnufake.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> list(Course course);

    Course selectById(Integer id);

    void insert(Course course);

    void update(Course course);

    void delete(Integer id);
}