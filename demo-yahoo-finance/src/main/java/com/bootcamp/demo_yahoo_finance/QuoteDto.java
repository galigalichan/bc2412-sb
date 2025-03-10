package com.bootcamp.demo_yahoo_finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuoteDto {
    private QuoteResponse quoteResponse;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuoteResponse {
        private Result[] result;
        private Object error;

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Result {
        private String language;
        private String region;
        private String quoteType;
        private String typeDisp;
        private String quoteSourceName;
        private boolean triggerable;
        private String customPriceAlertConfidence;
        private String currency;
        private String marketState;
        private String exchange;
        private String messageBoardID;
        private String exchangeTimezoneName;
        private String exchangeTimezoneShortName;
        private long gmtOffSetMilliseconds;
        private String market;
        private boolean esgPopulated;
        private double regularMarketChangePercent;
        private double regularMarketPrice;
        private CorporateAction[] corporateActions;
        private long regularMarketTime;
        private String longName;
        private String shortName;
        private boolean hasPrePostMarketData;
        private long firstTradeDateMilliseconds;
        private long priceHint;
        private double priceToBook;
        private long sourceInterval;
        private long exchangeDataDelayedBy;
        private String averageAnalystRating;
        private boolean tradeable;
        private boolean cryptoTradeable;
        private double regularMarketChange;
        private double regularMarketDayHigh;
        private String regularMarketDayRange;
        private double regularMarketDayLow;
        private long regularMarketVolume;
        private double regularMarketPreviousClose;
        private double bid;
        private double ask;
        private long bidSize;
        private long askSize;
        private String fullExchangeName;
        private String financialCurrency;
        private long regularMarketOpen;
        private long averageDailyVolume3Month;
        private long averageDailyVolume10Day;
        private double fiftyTwoWeekLowChange;
        private double fiftyTwoWeekLowChangePercent;
        private String fiftyTwoWeekRange;
        private long fiftyTwoWeekHighChange;
        private double fiftyTwoWeekHighChangePercent;
        private double fiftyTwoWeekLow;
        private double fiftyTwoWeekHigh;
        private double fiftyTwoWeekChangePercent;
        private long earningsTimestamp;
        private long earningsTimestampStart;
        private long earningsTimestampEnd;
        private long earningsCallTimestampStart;
        private long earningsCallTimestampEnd;
        private boolean isEarningsDateEstimate;
        private double trailingAnnualDividendRate;
        private double trailingPE;
        private double dividendRate;
        private double trailingAnnualDividendYield;
        private double dividendYield;
        private double epsTrailingTwelveMonths;
        private double epsForward;
        private double epsCurrentYear;
        private double priceEpsCurrentYear;
        private long sharesOutstanding;
        private double bookValue;
        private double fiftyDayAverage;
        private double fiftyDayAverageChange;
        private double fiftyDayAverageChangePercent;
        private double twoHundredDayAverage;
        private double twoHundredDayAverageChange;
        private double twoHundredDayAverageChangePercent;
        private long marketCap;
        private double forwardPE;
        private String symbol;

            @Getter
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class CorporateAction {
            private String header;
            private String message;
            private Meta meta;
        
                @Getter
                @Builder
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Meta {
                    private String eventType;
                    private long dateEpochMS;
                    private String amount;
                }

            }
        }
    }
}
