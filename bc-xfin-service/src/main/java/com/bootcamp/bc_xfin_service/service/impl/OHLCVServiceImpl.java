package com.bootcamp.bc_xfin_service.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.entity.OHLCVEntity;
import com.bootcamp.bc_xfin_service.lib.YahooFinanceManager;
import com.bootcamp.bc_xfin_service.model.dto.OhlcvRecordDTO;
import com.bootcamp.bc_xfin_service.model.dto.YahooFinanceChartDto;
import com.bootcamp.bc_xfin_service.repository.OHLCVRepository;
import com.bootcamp.bc_xfin_service.service.HolidayService;
import com.bootcamp.bc_xfin_service.service.OHLCVService;

import jakarta.transaction.Transactional;

@Service
public class OHLCVServiceImpl implements OHLCVService {
    @Autowired
    private OHLCVRepository ohlcvRepository;

    @Autowired
    private YahooFinanceManager yahooFinanceManager;

    @Autowired
    private HolidayService holidayService;

    @Transactional
    @Scheduled(cron = "0 0 18 * * MON-FRI") // Every weekday at 6:00 PM
    public void fetchAndSaveDailyOHLCV() {
        LocalDate today = LocalDate.now();
        if (holidayService.isHoliday(today)) {
            return; // Skip holidays
        }

        long startEpoch = today.atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond();
        long endEpoch = startEpoch + 86400;

        List<String> stocks = List.of("0388.HK", "0700.HK", "0005.HK", "0939.HK", "1299.HK", 
                                    "1398.HK", "0941.HK", "1211.HK", "0833.HK", "0016.HK");

        for (String symbol : stocks) {
            YahooFinanceChartDto chartDto = yahooFinanceManager.getHistoricalData(symbol, startEpoch, endEpoch);
            List<OhlcvRecordDTO> dtos = mapToOhlcvRecords(chartDto);
            saveAll(dtos);
        }
    }

    public void fetchAndSaveHistorical() {
        List<String> stocks = List.of(
            "0388.HK", "0700.HK", "0005.HK", "0939.HK", "1299.HK",
            "1398.HK", "0941.HK", "1211.HK", "0833.HK", "0016.HK"
        );

        Long endpoint = Instant.now().getEpochSecond(); // Current time
        Long startingPoint = 1420099200L; // Thu Jan 01 2015 16:00:00 GMT+0800

        for (String symbol : stocks) {
            YahooFinanceChartDto dto = yahooFinanceManager.getHistoricalData(symbol, startingPoint, endpoint);
            List<OhlcvRecordDTO> records = mapToOhlcvRecords(dto);
            saveAll(records);
        }
    }    

    public void saveAll(List<OhlcvRecordDTO> dtos) {
        List<OHLCVEntity> entities = dtos.stream()
            .map(dto -> {
                OHLCVEntity entity = new OHLCVEntity();
                entity.setSymbol(dto.getSymbol());
                entity.setTimestamp(dto.getTimestamp());
                entity.setOpen(dto.getOpen());
                entity.setHigh(dto.getHigh());
                entity.setLow(dto.getLow());
                entity.setClose(dto.getClose());
                entity.setVolume(dto.getVolume());
                return entity;
            })
            .collect(Collectors.toList());

        ohlcvRepository.saveAll(entities);
    }

    public List<OhlcvRecordDTO> mapToOhlcvRecords(YahooFinanceChartDto dto) {
        List<OhlcvRecordDTO> records = new ArrayList<>();

        if (dto == null || dto.getChart() == null || dto.getChart().getResult() == null || dto.getChart().getResult().isEmpty()) {
            return records;
        }

        YahooFinanceChartDto.Result result = dto.getChart().getResult().get(0);
        String symbol = result.getMeta().getSymbol();

        List<Long> timestamps = result.getTimestamp();
        List<Double> opens = result.getIndicators().getQuote().get(0).getOpen();
        List<Double> highs = result.getIndicators().getQuote().get(0).getHigh();
        List<Double> lows = result.getIndicators().getQuote().get(0).getLow();
        List<Double> closes = result.getIndicators().getQuote().get(0).getClose();
        List<Long> volumes = result.getIndicators().getQuote().get(0).getVolume();

        for (int i = 0; i < timestamps.size(); i++) {
            records.add(OhlcvRecordDTO.builder()
                .symbol(symbol)
                .timestamp(timestamps.get(i))
                .open(opens.get(i))
                .high(highs.get(i))
                .low(lows.get(i))
                .close(closes.get(i))
                .volume(volumes.get(i))
                .build());
        }

        return records;
    }
}
