package com.bootcamp.bc_xfin_web.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.bc_xfin_web.dto.CandleStickDTO;
import com.bootcamp.bc_xfin_web.dto.LinePointDTO;
import com.bootcamp.bc_xfin_web.model.CandleStick;
import com.bootcamp.bc_xfin_web.model.LinePoint;

@Component
public class ChartMapper {
    public CandleStickDTO map(CandleStick candle) {
        return CandleStickDTO.builder()
            .date(candle.getDate())
            .timestamp(candle.getTimestamp())
            .open(candle.getOpen())
            .high(candle.getHigh())
            .low(candle.getLow())
            .close(candle.getClose())
            .volume(candle.getVolume())
            .sma10(candle.getSma10())
            .sma20(candle.getSma20())
            .sma30(candle.getSma30())
            .sma50(candle.getSma50())
            .sma100(candle.getSma100())
            .sma150(candle.getSma150())
            .build();
    }

    // public CandleStickDTO map(CandleStick candle) {
    //   long unixtimeDate = DateManager.of(Zone.HK).convert(candle.getDate());
    //   return CandleStickDTO.builder() //
    //       .date(unixtimeDate) //
    //       .open(candle.getOpen()) //
    //       .close(candle.getClose()) //
    //       .high(candle.getHigh()) //
    //       .low(candle.getLow()) //
    //       .build();
    // }
    
    public LinePointDTO map(LinePoint point) {
        return LinePointDTO.builder()
            .dateTime(point.getTimestamp()) // Use existing timestamp
            .close(point.getClose())
            .movingAverage(point.getMovingAverage())
            .build();
    }

    // public LinePointDTO map(LinePoint point) {
    //   long unixtimeDatetime =
    //       DateManager.of(Zone.HK).convert(point.getDateTime());
    //   return LinePointDTO.builder() //
    //       .dateTime(unixtimeDatetime) //
    //       .close(point.getClose()) //
    //       .build();
    // }
}
