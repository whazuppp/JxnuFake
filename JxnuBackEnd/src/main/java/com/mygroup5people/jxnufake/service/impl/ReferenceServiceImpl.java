package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.entity.*;
import com.mygroup5people.jxnufake.mapper.ReferenceMapper;
import com.mygroup5people.jxnufake.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {
    private final ReferenceMapper referenceMapper;
    public List<Semester> semesters() { return referenceMapper.listSemesters(); }
    public List<AdministrativeClass> classes() { return referenceMapper.listClasses(); }
    public List<Classroom> classrooms() { return referenceMapper.listClassrooms(); }
    public List<ClassPeriod> classPeriods() { return referenceMapper.listClassPeriods(); }
}
