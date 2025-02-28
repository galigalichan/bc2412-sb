package com.bootcamp.sb.demo_sb_coingecko.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bootcamp.sb.demo_sb_coingecko.model.CoinMarket;
import com.bootcamp.sb.demo_sb_coingecko.model.dto.CoinGeckoMarketDto;

@Component
public class CoreMapper {
  @Autowired
  private ModelMapper modelMapper;

  public CoinMarket map(CoinGeckoMarketDto dto) {
    return this.modelMapper.map(dto, CoinMarket.class);
  }
}
