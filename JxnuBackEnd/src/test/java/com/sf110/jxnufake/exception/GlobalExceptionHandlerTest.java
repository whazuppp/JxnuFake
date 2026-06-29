package com.mygroup5people.jxnufake.exception;

import com.mygroup5people.jxnufake.pojo.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GlobalExceptionHandlerTest {

    @Test
    void businessExceptionReturnsDocumentedErrorResult() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        Result result = handler.handleBusinessException(new BusinessException("课程容量已满"));

        assertEquals(0, result.getCode());
        assertEquals("课程容量已满", result.getMsg());
        assertNull(result.getData());
    }
}
