package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.dto.OfferingRequest;
import com.mygroup5people.jxnufake.dto.ScheduleRequest;
import com.mygroup5people.jxnufake.entity.AdministrativeClass;
import com.mygroup5people.jxnufake.entity.Classroom;
import com.mygroup5people.jxnufake.entity.Course;
import com.mygroup5people.jxnufake.entity.Semester;
import com.mygroup5people.jxnufake.entity.Teacher;
import com.mygroup5people.jxnufake.exception.BusinessException;
import com.mygroup5people.jxnufake.mapper.CourseMapper;
import com.mygroup5people.jxnufake.mapper.OfferingMapper;
import com.mygroup5people.jxnufake.mapper.ReferenceMapper;
import com.mygroup5people.jxnufake.mapper.TeacherMapper;
import com.mygroup5people.jxnufake.vo.OfferingVO;
import com.mygroup5people.jxnufake.vo.StudentSummaryVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class OfferingServiceImplTest {

    @Mock
    private OfferingMapper offeringMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private ReferenceMapper referenceMapper;

    @InjectMocks
    private OfferingServiceImpl offeringService;

    @BeforeEach
    void setUpReferences() {
        Course course = new Course();
        course.setId(8);
        course.setWeeklyPeriods(2);
        lenient().when(courseMapper.selectById(8)).thenReturn(course);

        Teacher teacher = new Teacher();
        teacher.setId(3);
        lenient().when(teacherMapper.selectById(3)).thenReturn(teacher);

        AdministrativeClass administrativeClass = new AdministrativeClass();
        administrativeClass.setId(2);
        lenient().when(referenceMapper.selectClassById(2)).thenReturn(administrativeClass);

        Semester semester = new Semester();
        semester.setId(1);
        lenient().when(referenceMapper.selectSemesterById(1)).thenReturn(semester);

        Classroom classroom = new Classroom();
        classroom.setId(5);
        lenient().when(referenceMapper.selectClassroomById(5)).thenReturn(classroom);

        lenient().doAnswer(invocation -> {
            invocation.<com.mygroup5people.jxnufake.entity.CourseOffering>getArgument(0).setId(12);
            return null;
        }).when(offeringMapper).insertOffering(any());
        OfferingVO saved = new OfferingVO();
        saved.setId(12);
        lenient().when(offeringMapper.selectById(12)).thenReturn(saved);
        lenient().when(offeringMapper.listSchedules(12)).thenReturn(List.of());
    }

    @Test
    void rejectsOnePeriodSchedule() {
        OfferingRequest request = request(schedule(2, 3, 3));

        BusinessException exception = assertThrows(
                BusinessException.class, () -> offeringService.create(request)
        );

        assertEquals("每个排课时段必须连续占用2或3节", exception.getMessage());
        verify(offeringMapper, never()).insertOffering(any());
    }

    @Test
    void rejectsFourPeriodSchedule() {
        OfferingRequest request = request(schedule(2, 3, 6));

        BusinessException exception = assertThrows(
                BusinessException.class, () -> offeringService.create(request)
        );

        assertEquals("每个排课时段必须连续占用2或3节", exception.getMessage());
    }

    @Test
    void acceptsTwoPeriodSchedule() {
        OfferingRequest request = request(schedule(2, 3, 4));

        assertDoesNotThrow(() -> offeringService.create(request));

        verify(offeringMapper).insertOffering(any());
        verify(offeringMapper).insertSchedule(any());
    }

    @Test
    void rejectsWeeklyPeriodTotalMismatch() {
        Course course = courseMapper.selectById(8);
        course.setWeeklyPeriods(3);
        OfferingRequest request = request(schedule(2, 3, 4));

        BusinessException exception = assertThrows(
                BusinessException.class, () -> offeringService.create(request)
        );

        assertEquals("排课总节数必须等于课程每周课时", exception.getMessage());
    }

    @Test
    void rejectsClassroomOverlap() {
        when(offeringMapper.countClassroomConflicts(1, 2, 3, 4, 5, null)).thenReturn(1);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> offeringService.create(request(schedule(2, 3, 4)))
        );

        assertEquals("教室在所选时间段已被占用", exception.getMessage());
    }

    @Test
    void rejectsTeacherOverlap() {
        when(offeringMapper.countTeacherConflicts(1, 2, 3, 4, 3, null)).thenReturn(1);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> offeringService.create(request(schedule(2, 3, 4)))
        );

        assertEquals("教师在所选时间段已有课程", exception.getMessage());
    }

    @Test
    void rejectsAdministrativeClassOverlap() {
        when(offeringMapper.countClassConflicts(1, 2, 3, 4, 2, null)).thenReturn(1);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> offeringService.create(request(schedule(2, 3, 4)))
        );

        assertEquals("行政班在所选时间段已有课程", exception.getMessage());
    }

    @Test
    void acceptsAdjacentNonOverlappingPeriods() {
        OfferingRequest request = request(schedule(2, 3, 4));

        assertDoesNotThrow(() -> offeringService.create(request));

        verify(offeringMapper).countClassroomConflicts(1, 2, 3, 4, 5, null);
        verify(offeringMapper).countTeacherConflicts(1, 2, 3, 4, 3, null);
        verify(offeringMapper).countClassConflicts(1, 2, 3, 4, 2, null);
    }

    @Test
    void returnsStudentAvatarInCourseRoster() {
        OfferingVO offering = new OfferingVO();
        offering.setId(10);
        StudentSummaryVO student = new StudentSummaryVO();
        student.setImage("https://example.com/avatar.jpg");
        when(offeringMapper.selectById(10)).thenReturn(offering);
        when(offeringMapper.countStudentEnrollment(1, 10)).thenReturn(1);
        when(offeringMapper.listStudents(10)).thenReturn(List.of(student));

        List<StudentSummaryVO> students = offeringService.listStudents(10, 1);

        assertEquals("https://example.com/avatar.jpg", students.get(0).getImage());
    }

    private OfferingRequest request(ScheduleRequest... schedules) {
        OfferingRequest request = new OfferingRequest();
        request.setCourseId(8);
        request.setTeacherId(3);
        request.setClassId(2);
        request.setSemesterId(1);
        request.setCapacity(60);
        request.setSchedules(List.of(schedules));
        return request;
    }

    private ScheduleRequest schedule(int weekday, int startPeriod, int endPeriod) {
        ScheduleRequest request = new ScheduleRequest();
        request.setWeekday(weekday);
        request.setStartPeriod(startPeriod);
        request.setEndPeriod(endPeriod);
        request.setClassroomId(5);
        return request;
    }
}
