package com.mygroup5people.jxnufake.mapper;

import com.mygroup5people.jxnufake.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {
    List<Teacher> list(Teacher filter);
    Teacher selectById(Integer id);
    void insert(Teacher teacher);
    void update(Teacher teacher);
    void delete(Integer id);
    Integer countOfferings(Integer id);
}
