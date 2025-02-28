package com.bootcamp.sb.demo_sb_coingecko.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CoinMarket {
  private String id;
  private String symbol;
  private String name;
  private String image;
  private Double currentPrice;
  private Double marketCap;
  private Integer marketCapRank;
  private Double totalVolume;
  private Double high24h;
  private Double low24h;
  private Double priceChange24h;
  private Double priceChangePercentage24h;
  private Long circulatingSupply;
  private Long totalSupply;
  private Long maxSupply;
  private LocalDateTime lastUpdated;
}