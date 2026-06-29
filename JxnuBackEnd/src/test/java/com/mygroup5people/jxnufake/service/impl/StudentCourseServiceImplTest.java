package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.exception.BusinessException;
import com.mygroup5people.jxnufake.mapper.OfferingMapper;
import com.mygroup5people.jxnufake.mapper.ReferenceMapper;
import com.mygroup5people.jxnufake.mapper.StuMapper;
import com.mygroup5people.jxnufake.mapper.StudentCourseMapper;
import com.mygroup5people.jxnufake.vo.OfferingVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCourseServiceImplTest {
    @Mock private StudentCourseMapper studentCourseMapper;
    @Mock private OfferingMapper offeringMapper;
    @Mock private StuMapper stuMapper;
    @Mock private ReferenceMapper referenceMapper;
    @InjectMocks private StudentCourseServiceImpl service;

    @Test
    void selectsAvailableOffering() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 35));

        service.select(7, 12);

        verify(studentCourseMapper).countSelectedCourse(7, 8, 1);
        verify(studentCourseMapper).insert(7, 12);
    }

    @Test
    void rejectsFullOffering() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 60));

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("课程容量已满", error.getMessage());
        verify(studentCourseMapper, never()).insert(7, 12);
    }

    @Test
    void rejectsDuplicateOffering() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 35));
        when(studentCourseMapper.countSelectedOffering(7, 12)).thenReturn(1);

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("不能重复选择同一开课班", error.getMessage());
    }

    @Test
    void rejectsSameCourseThroughAnotherOfferingInSameSemester() {
        when(studentCourseMapper.lockOffering(12)).thenReturn(offering(12, 8, 1, 60, 35));
        when(studentCourseMapper.countSelectedCourse(7, 8, 1)).thenReturn(1);

        BusinessException error = assertThrows(BusinessException.class, () -> service.select(7, 12));

        assertEquals("同一课程只能选择一个开课班", error.getMessage());
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
        offering.setCapacity(capacity);
        offering.setStudentCount(studentCount);
        return offering;
    }
}
