package com.bootcamp.sb.demo_sb_bccalculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleNumberFormatException() {
        return ErrorResult.builder().code(9).message("Invalid Input.").build();
    }

    @ExceptionHandler(value = OperationArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleOperationArgumentException() {
        return ErrorResult.builder().code(9).message("Invalid Input.").build();
    }
}
