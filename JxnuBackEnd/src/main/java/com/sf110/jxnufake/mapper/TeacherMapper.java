package com.sf110.jxnufake.mapper;

import com.sf110.jxnufake.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherMapper {
    List<Teacher> list(@Param("field") String field,
                       @Param("keyword") String keyword,
                       @Param("exact") boolean exact);
    Teacher selectById(Integer id);
    void insert(Teacher teacher);
    void update(Teacher teacher);
    void delete(Integer id);
    Integer countOfferings(Integer id);
}
