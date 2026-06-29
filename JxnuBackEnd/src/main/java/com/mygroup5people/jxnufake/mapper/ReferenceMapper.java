package com.mygroup5people.jxnufake.mapper;

import com.mygroup5people.jxnufake.entity.AdministrativeClass;
import com.mygroup5people.jxnufake.entity.ClassPeriod;
import com.mygroup5people.jxnufake.entity.Classroom;
import com.mygroup5people.jxnufake.entity.Semester;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReferenceMapper {
    List<Semester> listSemesters();
    List<AdministrativeClass> listClasses();
    List<Classroom> listClassrooms();
    List<ClassPeriod> listClassPeriods();
    Semester selectSemesterById(Integer id);
    AdministrativeClass selectClassById(Integer id);
    Classroom selectClassroomById(Integer id);
}
