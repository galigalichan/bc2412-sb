package com.bootcamp.sb.demo_sb_coingecko.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(value = SnakeCaseStrategy.class)
public class CoinGeckoMarketDto {
  private String id;
  private String symbol;
  private String name;
  private String image;
  private Double currentPrice;
  private Double marketCap;
  private Integer marketCapRank;
  private Double fullyDilutedValuation;
  private Double totalVolume;
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
  private Long circulatingSupply;
  private Long totalSupply;
  private Long maxSupply;
  private Double ath;
  private Double athChangePercentage;
  private String athDate;
  private Double atl;
  private Double atlChangePercentage;
  private String atlDate;
  private Roi roi;
  private LocalDateTime lastUpdated;

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Roi {
    private Double times;
    private String currency;
    private Double percentage;
  }

}