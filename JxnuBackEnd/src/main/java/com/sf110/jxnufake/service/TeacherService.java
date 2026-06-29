package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.dto.TeacherRequest;
import com.mygroup5people.jxnufake.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> list(Teacher filter);
    Teacher get(Integer id);
    Teacher create(TeacherRequest request);
    Teacher update(Integer id, TeacherRequest request);
    void delete(Integer id);
}
