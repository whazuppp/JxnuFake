package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.dto.TeacherRequest;
import com.mygroup5people.jxnufake.entity.Teacher;
import com.mygroup5people.jxnufake.exception.BusinessException;
import com.mygroup5people.jxnufake.mapper.TeacherMapper;
import com.mygroup5people.jxnufake.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherMapper teacherMapper;

    @Override
    public List<Teacher> list(Teacher filter) {
        return teacherMapper.list(filter);
    }

    @Override
    public Teacher get(Integer id) {
        Teacher teacher = teacherMapper.selectById(id);
        if (teacher == null) throw new BusinessException("教师不存在");
        return teacher;
    }

    @Override
    public Teacher create(TeacherRequest request) {
        Teacher teacher = fromRequest(request);
        teacher.setCreateTime(LocalDateTime.now());
        teacher.setUpdateTime(LocalDateTime.now());
        teacherMapper.insert(teacher);
        return teacher;
    }

    @Override
    public Teacher update(Integer id, TeacherRequest request) {
        get(id);
        Teacher teacher = fromRequest(request);
        teacher.setId(id);
        teacher.setUpdateTime(LocalDateTime.now());
        teacherMapper.update(teacher);
        return teacherMapper.selectById(id);
    }

    @Override
    public void delete(Integer id) {
        if (teacherMapper.countOfferings(id) > 0) {
            throw new BusinessException("教师已被开课班引用，不能删除");
        }
        teacherMapper.delete(id);
    }

    private Teacher fromRequest(TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setTeacherNo(request.getTeacherNo());
        teacher.setName(request.getName());
        teacher.setGender(request.getGender());
        teacher.setTitle(request.getTitle());
        return teacher;
    }
}
