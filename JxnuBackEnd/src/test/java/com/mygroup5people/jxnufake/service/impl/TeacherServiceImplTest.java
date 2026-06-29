package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.exception.BusinessException;
import com.mygroup5people.jxnufake.mapper.TeacherMapper;
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
}
