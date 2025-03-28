package com.bootcamp.bc_xfin_web.controller;

import java.util.List;

import com.bootcamp.bc_xfin_web.dto.LinePointDTO;

public interface LineChartOperation {
    /**
     * 
     * @param type i.e. type=FIVE_MINUTE
     * @return List<PricePoint>
     */
    List<LinePointDTO> getLineChart(String symbol, String interval);
}