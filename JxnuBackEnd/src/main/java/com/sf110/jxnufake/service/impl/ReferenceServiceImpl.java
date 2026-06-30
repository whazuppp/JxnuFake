package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.entity.*;
import com.sf110.jxnufake.entity.AdministrativeClass;
import com.sf110.jxnufake.entity.ClassPeriod;
import com.sf110.jxnufake.entity.Classroom;
import com.sf110.jxnufake.entity.Semester;
import com.sf110.jxnufake.mapper.ReferenceMapper;
import com.sf110.jxnufake.service.ReferenceService;
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
