package com.bootcamp.sb.demo_sb_bccalculator.dto;

import lombok.Builder;
import lombok.Getter;

@Getter // for serialization
@Builder
public class CalRespDTO {
    private String x;
    private String y;
    private String operation;
    private String result;
}
