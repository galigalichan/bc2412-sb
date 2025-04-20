package com.bootcamp.bc_xfin_web.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CandleStickDTO {
    private LocalDate date;
    private Long timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
    private Double sma10;
    private Double sma20;
    private Double sma30;
    private Double sma50;
    private Double sma100;
    private Double sma150;
}
  
