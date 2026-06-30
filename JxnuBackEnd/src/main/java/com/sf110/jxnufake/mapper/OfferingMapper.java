package com.sf110.jxnufake.mapper;

import com.sf110.jxnufake.entity.CourseOffering;
import com.sf110.jxnufake.entity.CourseSchedule;
import com.sf110.jxnufake.vo.OfferingVO;
import com.sf110.jxnufake.vo.ScheduleVO;
import com.sf110.jxnufake.vo.StudentSummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OfferingMapper {
    List<OfferingVO> list(@Param("semesterId") Integer semesterId, @Param("courseName") String courseName,
                          @Param("teacherName") String teacherName, @Param("classId") Integer classId);
    OfferingVO selectById(Integer id);
    List<ScheduleVO> listSchedules(Integer offeringId);
    void insertOffering(CourseOffering offering);
    void updateOffering(CourseOffering offering);
    void deleteOffering(Integer id);
    void deleteSchedules(Integer offeringId);
    void insertSchedule(CourseSchedule schedule);
    Integer countClassroomConflicts(@Param("semesterId") Integer semesterId, @Param("weekday") Integer weekday,
        @Param("startPeriod") Integer startPeriod, @Param("endPeriod") Integer endPeriod,
        @Param("classroomId") Integer classroomId, @Param("excludeOfferingId") Integer excludeOfferingId);
    Integer countTeacherConflicts(@Param("semesterId") Integer semesterId, @Param("weekday") Integer weekday,
        @Param("startPeriod") Integer startPeriod, @Param("endPeriod") Integer endPeriod,
        @Param("teacherId") Integer teacherId, @Param("excludeOfferingId") Integer excludeOfferingId);
    Integer countClassConflicts(@Param("semesterId") Integer semesterId, @Param("weekday") Integer weekday,
        @Param("startPeriod") Integer startPeriod, @Param("endPeriod") Integer endPeriod,
        @Param("classId") Integer classId, @Param("excludeOfferingId") Integer excludeOfferingId);
    Integer countEnrollments(Integer offeringId);
    Integer countStudentEnrollment(@Param("studentId") Integer studentId, @Param("offeringId") Integer offeringId);
    List<StudentSummaryVO> listStudents(Integer offeringId);
}
