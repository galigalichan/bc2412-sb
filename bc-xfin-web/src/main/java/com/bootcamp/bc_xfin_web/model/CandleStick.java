package com.bootcamp.bc_xfin_web.model;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class CandleStick {
    private LocalDate date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
  
    public CandleStick(int year, int month, int dayOfMonth, Double open,
        Double high, Double low, Double close, Long volume) {
      this.date = LocalDate.of(year, month, dayOfMonth);
      this.open = open;
      this.high = high;
      this.low = low;
      this.close = close;
      this.volume = volume;
    }
  
    public enum TYPE {
      DAY;
  
      public static CandleStick.TYPE of(String type) {
        return switch (type) {
          case "1d" -> CandleStick.TYPE.DAY;
          default -> throw new RuntimeException();
        };
      }
    }
}
