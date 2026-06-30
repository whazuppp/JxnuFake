package com.sf110.jxnufake.service;

import com.sf110.jxnufake.dto.TeacherRequest;
import com.sf110.jxnufake.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> list(String field, String keyword, boolean exact);
    Teacher get(Integer id);
    Teacher create(TeacherRequest request);
    Teacher update(Integer id, TeacherRequest request);
    void delete(Integer id);
}
