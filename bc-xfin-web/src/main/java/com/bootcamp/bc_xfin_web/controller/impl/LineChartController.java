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
import com.bootcamp.bc_xfin_web.dto.LineChartDTO;
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
  public LineChartDTO getLineChart(@RequestParam String symbol, @RequestParam String interval) {
    // Fetch both price points and moving averages from Redis/backend
    Map<String, List<LinePoint>> pricePointData = getPricePointByFiveMinute(symbol);

    List<LinePoint> pricePoints = pricePointData.getOrDefault("priceData", List.of());
    List<LinePoint> movingAverages = pricePointData.getOrDefault("movingAverage", List.of());

    // List<LinePoint> pricePoints = switch (LinePoint.TYPE.of(interval)) {
    //   case FIVE_MIN -> ((StockDataService) pricePointData).getPricePointByFiveMinute(symbol).getOrDefault("pricePoints", List.of());
    // };

    // List<LinePoint> movingAverages = switch (LinePoint.TYPE.of(interval)) {
    //   case FIVE_MIN -> ((StockDataService) pricePointData).getPricePointByFiveMinute(symbol).getOrDefault("movingAverage", List.of());
    // };

    // Convert to DTOs for the response
    List<LinePointDTO> pricePointDTOs = pricePoints.stream()
        .map(chartMapper::map)
        .collect(Collectors.toList());

    List<LinePointDTO> movingAverageDTOs = movingAverages.stream()
        .map(chartMapper::map)
        .collect(Collectors.toList());

    return new LineChartDTO(pricePointDTOs, movingAverageDTOs);

  }

  private Map<String, List<LinePoint>> getPricePointByFiveMinute(String symbol) {
    return (Map<String, List<LinePoint>>) stockDataService.getPricePointByFiveMinute(symbol);
    
  }
  
}
