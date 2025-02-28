package com.bootcamp.sb.demo_sb_bccalculator.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResult {
    private Integer code;
    private String message;
}
