package com.bootcamp.bc_xfin_web.controller;

import java.util.List;

import com.bootcamp.bc_xfin_web.dto.CandleStickDTO;

public interface CandleChartOperation {
    /**
     * 
     * @param type i.e. type=DAY
     * @return List<CandleStick>
     */
    List<CandleStickDTO> getCandleChart(String symbol, String interval);
}
