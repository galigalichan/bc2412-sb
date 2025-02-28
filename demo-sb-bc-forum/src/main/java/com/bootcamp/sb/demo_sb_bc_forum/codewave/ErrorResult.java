package com.bootcamp.sb.demo_sb_bc_forum.codewave;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
