package com.sf110.jxnufake.service;

import com.sf110.jxnufake.entity.AdministrativeClass;
import com.sf110.jxnufake.entity.ClassPeriod;
import com.sf110.jxnufake.entity.Classroom;
import com.sf110.jxnufake.entity.Semester;

import java.util.List;

public interface ReferenceService {
    List<Semester> semesters();
    List<AdministrativeClass> classes();
    List<Classroom> classrooms();
    List<ClassPeriod> classPeriods();
}
