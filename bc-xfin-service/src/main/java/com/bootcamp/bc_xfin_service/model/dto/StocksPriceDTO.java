package com.bootcamp.bc_xfin_service.model.dto;

import java.time.LocalDateTime;

import com.bootcamp.bc_xfin_service.codewave.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StocksPriceDTO {
    private String symbol;
    private Long regularMarketTime;
    private Double regularMarketPrice;
    private Double regularMarketChangePercent;
    private Double bid;
    private Double ask;
    private Type type;
    private LocalDateTime currTime;
    private LocalDateTime marketUnixTime;
}
