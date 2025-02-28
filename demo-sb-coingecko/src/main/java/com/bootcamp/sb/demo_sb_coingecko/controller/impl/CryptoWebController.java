package com.bootcamp.sb.demo_sb_coingecko.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_coingecko.controller.CryptoWebOperation;
import com.bootcamp.sb.demo_sb_coingecko.dto.CryptoWebDTO;
import com.bootcamp.sb.demo_sb_coingecko.dto.mapper.DTOMapper;
import com.bootcamp.sb.demo_sb_coingecko.service.CoinGeckoService;
import com.fasterxml.jackson.core.JsonProcessingException;

// http://localhost:8085/crypto/api/v1/coin/market
@RestController
@RequestMapping("/crypto/api/v1")
public class CryptoWebController implements CryptoWebOperation{
    @Autowired
    private CoinGeckoService coinGeckoService;

    @Autowired
    private DTOMapper dtoMapper;

  @Override
  public List<CryptoWebDTO> getCoinMarket() {
    return coinGeckoService.getCoinMarket() //
        .stream() //
        .map(e -> dtoMapper.map(e)) //
        .collect(Collectors.toList());
  }

  @Override
  public List<CryptoWebDTO> getCoinMarketWithCache() throws JsonProcessingException {
    return coinGeckoService.getCoinMarketWithCache() //
        .stream() //
        .map(e -> dtoMapper.map(e)) //
        .collect(Collectors.toList());
  }
}
