package com.bootcamp.bc_xfin_service.model;

import java.time.Instant;
import java.time.LocalDateTime;
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
public class LinePoint {
    private LocalDateTime dateTime;
    private Long timestamp;
    private Double close;
    private Double movingAverage;  // New field for moving average
  
    public LinePoint(int year, int month, int dayOfMonth, int hour, int minute,
        Double close) {
        this.dateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, 0);
        this.timestamp = dateTime.atZone(ZoneId.of("Asia/Hong_Kong")).toEpochSecond(); // Convert to Unix timestamp
        this.close = close;
    }

    // New constructor to accept Unix timestamp
    public LinePoint(Long unixTimestamp, Double close, Double movingAverage) {
        this.timestamp = unixTimestamp; // Always preserve the original Unix timestamp
        this.dateTime = Instant.ofEpochSecond(unixTimestamp) // Convert to local time only for display
                              .atZone(ZoneId.of("Asia/Hong_Kong"))
                              .toLocalDateTime();
        this.close = close;
        this.movingAverage = movingAverage;  // Store calculated moving average
    }
  
    public enum TYPE {
      FIVE_MIN;
  
      public static LinePoint.TYPE of(String type) {
        return switch (type) {
          case "5m" -> LinePoint.TYPE.FIVE_MIN;
          default -> throw new RuntimeException("Invalid type: " + type);
        };
      }
    }

    @Override
    public String toString() {
        return "LinePoint{" +
                "dateTime=" + dateTime +
                ", timestamp=" + timestamp +
                ", close=" + close +
                ", movingAverage=" + movingAverage +
                '}';
    }    
}
