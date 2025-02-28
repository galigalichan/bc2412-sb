package com.bootcamp.sb.demo_sb_coingecko.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.bootcamp.sb.demo_sb_coingecko.dto.CryptoWebDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CryptoWebOperation {
  @GetMapping("/coin/market")
  List<CryptoWebDTO> getCoinMarket();

  @GetMapping("/cache/coin/market")
  List<CryptoWebDTO> getCoinMarketWithCache() throws JsonProcessingException;
}
