package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.entity.AdministrativeClass;
import com.mygroup5people.jxnufake.entity.ClassPeriod;
import com.mygroup5people.jxnufake.entity.Classroom;
import com.mygroup5people.jxnufake.entity.Semester;

import java.util.List;

public interface ReferenceService {
    List<Semester> semesters();
    List<AdministrativeClass> classes();
    List<Classroom> classrooms();
    List<ClassPeriod> classPeriods();
}
