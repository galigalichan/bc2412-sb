package com.bootcamp.bc_xfin_web.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandleStick {
    private LocalDate date;
    private Long timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
    private Double sma10;
    private Double sma20;
    private Double sma30; // for monthly charts only
    private Double sma50;
    private Double sma100;
    private Double sma150;
  
    public CandleStick(int year, int month, int dayOfMonth, Double open,
        Double high, Double low, Double close, Long volume) {
        this.date = LocalDate.of(year, month, dayOfMonth);
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public CandleStick(Long unixTimestamp, Double open, Double high, Double low, Double close, Long volume, 
        Double sma10, Double sma20, Double sma30, Double sma50, Double sma100, Double sma150) {
        this.timestamp = unixTimestamp; // Always preserve the original Unix timestamp
        this.date = Instant.ofEpochSecond(unixTimestamp) // Convert to local date for display
                        .atZone(ZoneId.of("Asia/Hong_Kong"))
                        .toLocalDate();
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.sma10 = sma10; // stored calculated SMA(10)
        this.sma20 =sma20; // stored calculated SMA(20)
        this.sma30 =sma30; // stored calculated SMA(30)
        this.sma50 = sma50; // stored calculated SMA(50)
        this.sma100 = sma100; // stored calculated SMA(100)
        this.sma150 = sma150; // stored calculated SMA(150)
    }
  
    public enum TYPE {
      DAY,
      WEEK,
      MONTH,
      ;
      
      public static CandleStick.TYPE of(String type) {
        return switch (type) {
          case "1d" -> CandleStick.TYPE.DAY;
          case "1w" -> CandleStick.TYPE.WEEK;
          case "1m" -> CandleStick.TYPE.MONTH;
          default -> throw new RuntimeException();
        };
      }
    }

    @Override
    public String toString() {
        return "CandleStick{" +
                "date=" + date +
                ", timestamp=" + timestamp +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", SMA(10)=" + sma10 +
                ", SMA(20)=" + sma20 +
                ", SMA(30)=" + sma30 +
                ", SMA(50)=" + sma50 +
                ", SMA(100)=" + sma100 +
                ", SMA(150)=" + sma150 +
                '}';
    }    
}
