package com.sf110.jxnufake.mapper;

import com.sf110.jxnufake.entity.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> list(Course course);

    Course selectById(Integer id);

    void insert(Course course);

    void update(Course course);

    void delete(Integer id);

    Integer countOfferings(Integer id);
}
