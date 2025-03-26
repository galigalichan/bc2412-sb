package com.bootcamp.bc_xfin_web.controller;

import com.bootcamp.bc_xfin_web.dto.LineChartDTO;

public interface LineChartOperation {
    /**
     * 
     * @param type i.e. type=FIVE_MINUTE
     * @return List<PricePoint>
     */
    LineChartDTO getLineChart(String symbol, String interval);
}