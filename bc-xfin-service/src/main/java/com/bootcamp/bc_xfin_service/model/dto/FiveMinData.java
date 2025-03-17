package com.bootcamp.bc_xfin_service.model.dto;

import java.util.List;

import com.bootcamp.bc_xfin_service.entity.TStocksPriceEntity;

public class FiveMinData {
    private long regularMarketTime;
    private List<TStocksPriceEntity> data;

    public FiveMinData(long regularMarketTime, List<TStocksPriceEntity> data) {
        this.regularMarketTime = regularMarketTime;
        this.data = data;
    }

    public long getRegularMarketTime() {
        return regularMarketTime;
    }

    public List<TStocksPriceEntity> getData() {
        return data;
    }
}
