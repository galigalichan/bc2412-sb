package com.bootcamp.sb.demo_sb_bc_forum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleBusinessException(BusinessException e, WebRequest request) {
        return new ErrorResult(e.getSysCode().getCode(), e.getSysCode().getMessage());

    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleRestClientException(RestClientException e, WebRequest request) {
        // Log the error details for debugging
        System.err.println("REST client error: " + e.getMessage());
        return new ErrorResult(SysCode.RESTTEMPLATE_EXCEPTION.getCode(), SysCode.RESTTEMPLATE_EXCEPTION.getMessage());

    }
}
