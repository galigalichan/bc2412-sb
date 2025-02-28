package com.bootcamp.sb.demo_sb_bc_forum.codewave;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // @ExceptionHandler(BusinessException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public ErrorResult handleBusinessException(BusinessException e, WebRequest request) {
    //     return new ErrorResult(e.getSysCode().getCode(), e.getSysCode().getMessage());

    // }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResp<Void> handleBusinessException(BusinessException e, WebRequest request) {
        return ApiResp.<Void>builder().syscode(e.getSysCode()).build();
        
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleRestClientException(RestClientException e, WebRequest request) {
        return new ErrorResult(SysCode.RESTTEMPLATE_EXCEPTION.getCode(), SysCode.RESTTEMPLATE_EXCEPTION.getMessage());

    }

    public <T> ApiResp<T> handleSuccessfulResponse(T data) {
        return ApiResp.<T>builder().syscode(SysCode.SUCCESS).data(data).build();
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResp<UserEntity> handleNumberFormatException() {
        return ApiResp.<UserEntity>builder().syscode(SysCode.API_UNAVAILABLE).build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResp<UserEntity> handleUserNotFoundException() {
        return ApiResp.<UserEntity>builder().syscode(SysCode.API_UNAVAILABLE).build();
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResp<List<PostDto>> handlePostNotFoundException() {
        return ApiResp.<List<PostDto>>builder().syscode(SysCode.API_UNAVAILABLE).build();
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResp<List<CommentDto>> handleCommentNotFoundException() {
        return ApiResp.<List<CommentDto>>builder().syscode(SysCode.API_UNAVAILABLE).build();
    }

        // @ExceptionHandler(value = UserNotFoundException.class)
        // @ResponseStatus(HttpStatus.BAD_REQUEST)
        // public ErrorResult handleUserNotFoundException() {
        //   return ErrorResult.builder() //
        //       .code(1L) //
        //       .message("User not found.") //
        //       .build();
        // }
      
        // @ExceptionHandler(value = NumberFormatException.class)
        // @ResponseStatus(HttpStatus.BAD_REQUEST)
        // public ErrorResult handleNumberFormatException() {
        //   return ErrorResult.builder() //
        //       .code(2L) //
        //       .message("Invalid Input.") //
        //       .build();
        // }
      
        // @ExceptionHandler(value = RestClientException.class)
        // @ResponseStatus(HttpStatus.BAD_REQUEST)
        // public ErrorResult handleRestClientException() {
        //   return ErrorResult.builder() //
        //       .code(3L) //
        //       .message("RestTemplate Error - JsonPlaceHolder.") //
        //       .build();
        // }

}
