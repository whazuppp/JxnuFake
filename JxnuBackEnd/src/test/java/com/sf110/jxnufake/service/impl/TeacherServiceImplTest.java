package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.exception.BusinessException;
import com.sf110.jxnufake.entity.Teacher;
import com.sf110.jxnufake.mapper.TeacherMapper;
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
class TeacherServiceImplTest {
    @Mock
    private TeacherMapper teacherMapper;
    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void referencedTeacherCannotBeDeleted() {
        when(teacherMapper.countOfferings(3)).thenReturn(1);

        BusinessException exception = assertThrows(
                BusinessException.class, () -> teacherService.delete(3)
        );

        assertEquals("教师已被开课班引用，不能删除", exception.getMessage());
        verify(teacherMapper, never()).delete(3);
    }

    @Test
    void teacherListPassesFilterAndFuzzyModeToMapper() {
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setTeacherNo("T2026");
        var expected = java.util.List.of(expectedTeacher);
        when(teacherMapper.list("teacherNo", "T2026", false)).thenReturn(expected);

        var actual = teacherService.list("teacherNo", "T2026", false);

        assertEquals(expected, actual);
        verify(teacherMapper).list("teacherNo", "T2026", false);
    }
}
