package com.bootcamp.bc_xfin_web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.bc_xfin_web.dto.CandleStickDTO;

public interface CandleChartOperation {

    /**
     * 
     * @param type i.e. type=DAY
     * @return List<CandleStick>
     */
    @GetMapping(value = "/chart/candle")
    List<CandleStickDTO> getCandleChart(@RequestParam String interval);
  
}
