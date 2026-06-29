package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.exception.BusinessException;
import com.mygroup5people.jxnufake.mapper.CourseMapper;
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
class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void referencedCourseCannotBeDeleted() {
        when(courseMapper.countOfferings(8)).thenReturn(1);

        BusinessException exception = assertThrows(
                BusinessException.class, () -> courseService.delete(8)
        );

        assertEquals("课程已被开课班引用，不能删除", exception.getMessage());
        verify(courseMapper, never()).delete(8);
    }
}
