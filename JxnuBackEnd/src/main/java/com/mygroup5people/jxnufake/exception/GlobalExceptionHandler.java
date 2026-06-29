package com.mygroup5people.jxnufake.exception;

import com.mygroup5people.jxnufake.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException exception) {
        log.warn("业务处理失败: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result handleValidationException(Exception exception) {
        String message;
        if (exception instanceof MethodArgumentNotValidException validException) {
            message = validException.getBindingResult().getFieldErrors().stream()
                    .findFirst().map(error -> error.getDefaultMessage()).orElse("请求参数不正确");
        } else {
            BindException bindException = (BindException) exception;
            message = bindException.getBindingResult().getFieldErrors().stream()
                    .findFirst().map(error -> error.getDefaultMessage()).orElse("请求参数不正确");
        }
        return Result.error(message);
    }

    @ExceptionHandler(Exception.class)
    public Result handleUnexpectedException(Exception exception) {
        log.error("服务器内部错误", exception);
        return Result.error("服务器内部错误");
    }
}
