package com.bootcamp.sb.demo_sb_coingecko.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bootcamp.sb.demo_sb_coingecko.dto.CryptoWebDTO;
import com.bootcamp.sb.demo_sb_coingecko.model.CoinMarket;

@Component
public class DTOMapper {
    @Autowired
    private ModelMapper modelMapper;

    public CryptoWebDTO map(CoinMarket coinMarket) {
        return this.modelMapper.map(coinMarket, CryptoWebDTO.class);
    }

    // public static CryptoWebDTO map(CryptoWebDTO e) {
        
    //     CryptoWebDTO.Roi coinRoi = e.getRoi() != null ? CryptoWebDTO.Roi.builder()
    //         .times(e.getRoi().getTimes())
    //         .currency(e.getRoi().getCurrency())
    //         .percentage(e.getRoi().getPercentage())
    //         .build() : null;
        
    //     return CryptoWebDTO.builder()
    //         .id(e.getId())
    //         .symbol(e.getSymbol())
    //         .name(e.getName())
    //         .image(e.getImage())
    //         .currentPrice(e.getCurrentPrice())
    //         .marketCap(e.getMarketCap())
    //         .marketCapRank(e.getMarketCapRank())
    //         .fullyDilutedValuation(e.getFullyDilutedValuation())
    //         .totalVolume(e.getTotalVolume())
    //         .high24h(e.getHigh24h())
    //         .low24h(e.getLow24h())
    //         .priceChange24h(e.getPriceChange24h())
    //         .priceChangePercentage24h(e.getPriceChangePercentage24h())
    //         .marketCapChange24h(e.getMarketCapChange24h())
    //         .marketCapChangePercentage24h(e.getMarketCapChangePercentage24h())
    //         .circulatingSupply(e.getCirculatingSupply())
    //         .totalSupply(e.getTotalSupply())
    //         .maxSupply(e.getMaxSupply())
    //         .ath(e.getAth())
    //         .athChangePercentage(e.getAthChangePercentage())
    //         .athDate(e.getAthDate())
    //         .atl(e.getAtl())
    //         .atlChangePercentage(e.getAtlChangePercentage())
    //         .atlDate(e.getAtlDate())
    //         .roi(coinRoi)
    //         .lastUpdated(e.getLastUpdated())
    //         .build();
    // }
}
