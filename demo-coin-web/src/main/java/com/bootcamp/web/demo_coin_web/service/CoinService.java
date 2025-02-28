package com.bootcamp.web.demo_coin_web.service;

import java.util.List;

import com.bootcamp.web.demo_coin_web.model.dto.CoinGeckoMarketDto;

public interface CoinService {
    List<CoinGeckoMarketDto> getCoinMarket();
}
