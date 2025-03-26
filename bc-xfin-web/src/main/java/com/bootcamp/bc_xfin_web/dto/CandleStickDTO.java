package com.bootcamp.bc_xfin_web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CandleStickDTO {
    private Long date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
}
  
