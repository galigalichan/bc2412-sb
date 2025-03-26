package com.bootcamp.bc_xfin_web.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.bc_xfin_web.dto.CandleStickDTO;
import com.bootcamp.bc_xfin_web.dto.LinePointDTO;
import com.bootcamp.bc_xfin_web.lib.DateManager;
import com.bootcamp.bc_xfin_web.lib.DateManager.Zone;
import com.bootcamp.bc_xfin_web.model.CandleStick;
import com.bootcamp.bc_xfin_web.model.LinePoint;

@Component
public class ChartMapper {
    public CandleStickDTO map(CandleStick candle) {
      long unixtimeDate = DateManager.of(Zone.HK).convert(candle.getDate());
      return CandleStickDTO.builder() //
          .date(unixtimeDate) //
          .open(candle.getOpen()) //
          .close(candle.getClose()) //
          .high(candle.getHigh()) //
          .low(candle.getLow()) //
          .build();
    }
    
    public LinePointDTO map(LinePoint point) {
        return LinePointDTO.builder()
            .dateTime(point.getTimestamp()) // Use existing timestamp
            .close(point.getClose())
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
