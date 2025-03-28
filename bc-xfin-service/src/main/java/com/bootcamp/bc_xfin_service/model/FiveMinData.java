package com.bootcamp.bc_xfin_service.model;

import java.util.List;

import com.bootcamp.bc_xfin_service.entity.TStocksPriceEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Jackson needs a no-args constructor to create an instance of FiveMinData before setting its fields.
@AllArgsConstructor
public class FiveMinData {
    private long regularMarketTime;

    @JsonProperty("data")
    private List<TStocksPriceEntity> data;

    public long getRegularMarketTime() {
        return regularMarketTime;
    }

    public List<TStocksPriceEntity> getData() {
        return data;
    }
}
