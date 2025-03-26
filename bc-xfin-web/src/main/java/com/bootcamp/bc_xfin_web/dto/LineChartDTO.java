package com.bootcamp.bc_xfin_web.dto;

import java.util.List;

public class LineChartDTO {
    private List<LinePointDTO> stockPrices;
    private List<LinePointDTO> movingAverages;

    public LineChartDTO(List<LinePointDTO> stockPrices, List<LinePointDTO> movingAverages) {
        this.stockPrices = stockPrices;
        this.movingAverages = movingAverages;
    }

    public List<LinePointDTO> getStockPrices() {
        return stockPrices;
    }

    public List<LinePointDTO> getMovingAverages() {
        return movingAverages;
    }    
}
