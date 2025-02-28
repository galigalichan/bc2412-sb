package com.bootcamp.sb.demo_sb_coingecko.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_coingecko.model.CoinMarket;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CoinGeckoService {
  List<CoinMarket> getCoinMarket();

  List<CoinMarket> getCoinMarketWithCache() throws JsonProcessingException;
}
