package com.bootcamp.bc_xfin_web.controller.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.bc_xfin_web.controller.LineChartOperation;
import com.bootcamp.bc_xfin_web.dto.LinePointDTO;
import com.bootcamp.bc_xfin_web.dto.mapper.ChartMapper;
import com.bootcamp.bc_xfin_web.model.LinePoint;
import com.bootcamp.bc_xfin_web.service.StockDataService;

@RestController
@RequestMapping(value = "/v1")
public class LineChartController implements LineChartOperation {
  @Autowired
  private ChartMapper chartMapper;

  @Autowired
  private StockDataService stockDataService;

  @Override
  @GetMapping(value = "/chart/line")
  public List<LinePointDTO> getLineChart(@RequestParam String symbol, @RequestParam String interval) {
    List<LinePoint> linePoints = switch (LinePoint.TYPE.of(interval)) {
      case FIVE_MIN -> getPricePointByFiveMinute(symbol).getOrDefault("linePoints", List.of());
    };
    return linePoints.stream()
        .map(chartMapper::map)
        .collect(Collectors.toList());
  }

  private Map<String, List<LinePoint>> getPricePointByFiveMinute(String symbol) {
    return (Map<String, List<LinePoint>>) stockDataService.getPricePointByFiveMinute(symbol);
    
  }
  
}
