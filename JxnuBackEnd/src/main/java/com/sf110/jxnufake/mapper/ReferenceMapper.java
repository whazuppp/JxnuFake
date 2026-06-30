package com.sf110.jxnufake.mapper;

import com.sf110.jxnufake.entity.AdministrativeClass;
import com.sf110.jxnufake.entity.ClassPeriod;
import com.sf110.jxnufake.entity.Classroom;
import com.sf110.jxnufake.entity.Semester;
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
