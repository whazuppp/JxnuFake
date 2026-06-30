package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.mapper.StuMapper;
import com.sf110.jxnufake.vo.StudentInfoVO;
import com.sf110.jxnufake.vo.StudentSummaryVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StuServiceImplTest {

    @Mock
    private StuMapper stuMapper;

    @InjectMocks
    private StuServiceImpl stuService;

    @Test
    void currentStudentInfoContainsClassButNeverPassword() {
        StudentInfoVO expected = new StudentInfoVO(
                7, "student", "林凯", (short) 1, null,
                LocalDate.of(2023, 9, 1), "202326202065",
                2, "23级计算机科学与技术2班"
        );
        when(stuMapper.getInfoById(7)).thenReturn(expected);

        StudentInfoVO actual = stuService.getInfo(7);

        assertEquals(expected, actual);
    }

    @Test
    void studentListPassesFilterAndExactModeToMapper() {
        StudentSummaryVO expectedStudent = new StudentSummaryVO();
        expectedStudent.setName("林凯");
        var expected = java.util.List.of(expectedStudent);
        when(stuMapper.list("name", "林凯", true)).thenReturn(expected);

        var actual = stuService.list("name", "林凯", true);

        assertEquals(expected, actual);
        verify(stuMapper).list("name", "林凯", true);
    }
}
