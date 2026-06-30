package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.exception.BusinessException;
import com.sf110.jxnufake.mapper.OfferingMapper;
import com.sf110.jxnufake.mapper.ReferenceMapper;
import com.sf110.jxnufake.mapper.StuMapper;
import com.sf110.jxnufake.mapper.StudentCourseMapper;
import com.sf110.jxnufake.vo.OfferingVO;
import com.sf110.jxnufake.vo.StudentInfoVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCourseServiceImplTest {
    @Mock
    private StudentCourseMapper studentCourseMapper;

    @Mock
    private OfferingMapper offeringMapper;

    @Mock
    private StuMapper stuMapper;

    @Mock
    private ReferenceMapper referenceMapper;

    @InjectMocks
    private StudentCourseServiceImpl service;

    @Test
    void selectsAvailableOffering() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 35));
        when(stuMapper.getInfoById(7)).thenReturn(student(7, 2));

        service.select(7, 12);

        verify(studentCourseMapper).countSelectedCourseInSemester(7, 8, 1);
        verify(studentCourseMapper).insert(7, 12);
    }

    @Test
    void rejectsFullOffering() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 60));
        when(stuMapper.getInfoById(7)).thenReturn(student(7, 2));

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("课程容量已满", error.getMessage());
        verify(studentCourseMapper, never()).insert(7, 12);
    }

    @Test
    void rejectsDuplicateOffering() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 35));
        when(stuMapper.getInfoById(7)).thenReturn(student(7, 2));
        when(studentCourseMapper.countSelectedOffering(7, 12)).thenReturn(1);

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("不能重复选择同一开课班", error.getMessage());
        verify(studentCourseMapper, never()).insert(7, 12);
    }

    @Test
    void rejectsSameCourseOnlyInSameSemester() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 35));
        when(stuMapper.getInfoById(7)).thenReturn(student(7, 2));
        when(studentCourseMapper.countSelectedCourseInSemester(7, 8, 1)).thenReturn(1);

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("同一学期内同一课程只能选择一个开课班", error.getMessage());
        verify(studentCourseMapper, never()).insert(7, 12);
    }

    @Test
    void rejectsOfferingForAnotherAdministrativeClass() {
        OfferingVO offering = offering(12, 8, 1, 60, 35);
        offering.setClassId(1);
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering);
        when(stuMapper.getInfoById(7)).thenReturn(student(7, 2));

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("所选课程与班级不匹配", error.getMessage());
        verify(studentCourseMapper, never()).insert(7, 12);
    }

    @Test
    void rejectsWithdrawalNotOwnedByCurrentStudent() {
        when(studentCourseMapper.deleteOwned(7, 12)).thenReturn(0);

        BusinessException error = assertThrows(BusinessException.class, () -> service.withdraw(7, 12));

        assertEquals("未选择该开课班，无法退课", error.getMessage());
    }

    private OfferingVO offering(int id, int courseId, int semesterId, int capacity, int studentCount) {
        OfferingVO offering = new OfferingVO();
        offering.setId(id);
        offering.setCourseId(courseId);
        offering.setSemesterId(semesterId);
        offering.setClassId(2);
        offering.setCapacity(capacity);
        offering.setStudentCount(studentCount);
        return offering;
    }

    private StudentInfoVO student(int id, int classId) {
        return new StudentInfoVO(
                id,
                "student",
                "测试学生",
                (short) 1,
                null,
                LocalDate.of(2023, 9, 1),
                "20230001",
                classId,
                "测试班级"
        );
    }
}
