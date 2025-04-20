package com.bootcamp.bc_xfin_web.controller.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.bc_xfin_web.controller.CandleChartOperation;
import com.bootcamp.bc_xfin_web.dto.CandleStickDTO;
import com.bootcamp.bc_xfin_web.dto.mapper.ChartMapper;
import com.bootcamp.bc_xfin_web.model.CandleStick;
import com.bootcamp.bc_xfin_web.service.OhlcvDataService;

@RestController
@RequestMapping(value = "/v1")
public class CandleChartController implements CandleChartOperation {
  @Autowired
  private ChartMapper chartMapper;

  @Autowired
  private OhlcvDataService ohlcvDataService;

  @Override
  @GetMapping(value = "/chart/candle")
  public List<CandleStickDTO> getCandleChart(@RequestParam String symbol, @RequestParam String interval) {
    List<CandleStick> candleSticks = switch (CandleStick.TYPE.of(interval)) {
      case DAY -> getCandlesByDay(symbol).getOrDefault("candleSticks", List.of());
      case WEEK -> getCandlesByWeek(symbol).getOrDefault("candleSticks", List.of());
      case MONTH -> getCandlesByMonth(symbol).getOrDefault("candleSticks", List.of());
    };
    return candleSticks.stream() //
        .map(e -> this.chartMapper.map(e)) //
        .collect(Collectors.toList());
  }

  private Map<String, List<CandleStick>> getCandlesByDay(String symbol) {
    return ohlcvDataService.getCandlesByDay(symbol);
  }

  private Map<String, List<CandleStick>> getCandlesByWeek(String symbol) {
    return ohlcvDataService.getCandlesByWeek(symbol);
  }

  private Map<String, List<CandleStick>> getCandlesByMonth(String symbol) {
    return ohlcvDataService.getCandlesByMonth(symbol);
  }
}
