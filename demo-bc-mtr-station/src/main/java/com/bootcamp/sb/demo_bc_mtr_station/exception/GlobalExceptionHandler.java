package com.bootcamp.sb.demo_bc_mtr_station.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getSysCode().getMessage());
    }

    @ExceptionHandler(NullInputException.class)
    public ResponseEntity<String> handleNullInputException(NullInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getSysCode().getMessage());
    }
    
}

