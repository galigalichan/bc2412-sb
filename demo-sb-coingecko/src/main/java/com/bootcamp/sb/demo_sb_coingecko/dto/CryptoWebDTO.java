package com.bootcamp.sb.demo_sb_coingecko.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ! Data Transfer Object
// This DTO is for deserialization (JSON -> OBJECT)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(value = SnakeCaseStrategy.class)
public class CryptoWebDTO implements Serializable {
    private String id;
    private String symbol;
    private String name;
    private String image;
    @JsonProperty(value = "current_price")
    private Long currentPrice;
    @JsonProperty(value = "market_cap")
    private Long marketCap;
    @JsonProperty(value = "market_cap_rank")
    private Integer marketCapRank;
    @JsonProperty(value = "fully_diluted_valuation")
    private Long fullyDilutedValuation;
    @JsonProperty(value = "total_volume")
    private Long totalVolume;
    @JsonProperty(value = "high_24h")
    private Double high24h;
    @JsonProperty(value = "low_24h")
    private Double low24h;
    @JsonProperty(value = "price_change_24h")
    private Double priceChange24h;
    @JsonProperty(value = "price_change_percentage_24h")
    private Double priceChangePercentage24h;
    @JsonProperty(value = "market_cap_change_24h")
    private Double marketCapChange24h;
    @JsonProperty(value = "market_cap_change_percentage_24h")
    private Double marketCapChangePercentage24h;
    @JsonProperty(value = "circulating_supply")
    private Long circulatingSupply;
    @JsonProperty(value = "total_supply")
    private Long totalSupply;
    @JsonProperty(value = "max_supply")
    private Long maxSupply;
    private Double ath;
    @JsonProperty(value = "ath_change_percentage")
    private Double athChangePercentage;
    @JsonProperty(value = "ath_date")   
    private Date athDate;
    private Double atl;
    @JsonProperty(value = "atl_change_percentage")    
    private Double atlChangePercentage;
    @JsonProperty(value = "atl_date") 
    private LocalDateTime atlDate;
    private Roi roi;
    @JsonProperty(value = "last_updated")     
    private LocalDateTime lastUpdated;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Roi {
        private Double times;
        private String currency;
        private Double percentage;
    }

}
