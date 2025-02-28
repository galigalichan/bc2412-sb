package com.bootcamp.sb.demo_sb_bccalculator.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalReqDTO {
    private String x;
    private String y;
    private String operation;   
}
