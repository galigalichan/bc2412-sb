package com.bootcamp.bc_xfin_service.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class YahooFinanceChartDto {

    private Chart chart;

    @Data
    public static class Chart {
        private List<Result> result;
        private Object error;
    }

    @Data
    public static class Result {
        private Meta meta;
        private List<Long> timestamp;
        private Indicators indicators;
    }

    @Data
    public static class Meta {
        private String currency;
        private String symbol;
        private String exchangeName;
        private String instrumentType;
        private Long firstTradeDate;
        private Long regularMarketTime;
        private Long gmtoffset;
        private String timezone;
        private String exchangeTimezoneName;
        private Double regularMarketPrice;
        private Double chartPreviousClose;
        private Long priceHint;
    }

    @Data
    public static class Indicators {
        private List<Quote> quote;
        private List<AdjClose> adjclose;
    }

    @Data
    public static class Quote {
        private List<Double> open;
        private List<Double> high;
        private List<Double> low;
        private List<Double> close;
        private List<Long> volume;
    }

    @Data
    public static class AdjClose {
        private List<Double> adjclose;
    }
}