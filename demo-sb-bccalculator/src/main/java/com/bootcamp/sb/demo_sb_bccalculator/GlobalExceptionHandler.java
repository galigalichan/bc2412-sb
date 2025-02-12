package com.bootcamp.sb.demo_sb_bccalculator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ArithmeticException.class, BusinessException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResult handleError(RuntimeException e) {
        return new ErrorResult(9, e.getMessage());
    }



}
