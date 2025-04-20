package com.bootcamp.bc_xfin_service.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.config.StocksProperties;
import com.bootcamp.bc_xfin_service.entity.OHLCVEntity;
import com.bootcamp.bc_xfin_service.lib.YahooFinanceManager;
import com.bootcamp.bc_xfin_service.model.CandleStick;
import com.bootcamp.bc_xfin_service.model.dto.OhlcvRecordDTO;
import com.bootcamp.bc_xfin_service.model.dto.YahooFinanceChartDto;
import com.bootcamp.bc_xfin_service.repository.OHLCVRepository;
import com.bootcamp.bc_xfin_service.service.HolidayService;
import com.bootcamp.bc_xfin_service.service.OHLCVService;
import com.bootcamp.bc_xfin_service.validation.AutoFixResult;
import com.bootcamp.bc_xfin_service.validation.OhlcvPatchSuggestion;

import jakarta.transaction.Transactional;

@Service
public class OHLCVServiceImpl implements OHLCVService {
    private static final Logger log = LoggerFactory.getLogger(OHLCVServiceImpl.class);

    private final OHLCVRepository ohlcvRepository;
    private final YahooFinanceManager yahooFinanceManager;
    private final HolidayService holidayService;
    private final StocksProperties stocksProperties;
    // private final DataSource dataSource;
    
    public OHLCVServiceImpl(OHLCVRepository ohlcvRepository,
                        YahooFinanceManager yahooFinanceManager,
                        HolidayService holidayService,
                        StocksProperties stockSymbolProperties) {
        this.ohlcvRepository = ohlcvRepository;
        this.yahooFinanceManager = yahooFinanceManager;
        this.holidayService = holidayService;
        this.stocksProperties = stockSymbolProperties;
    }

    @Transactional
    @Scheduled(cron = "0 0 21 * * MON-FRI")
    public void fetchAndSaveDailyOHLCV() {
        LocalDate today = LocalDate.now();
        if (holidayService.isHoliday(today)) {
            return; // Skip holidays
        }

        long startEpoch = today.atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond();
        long endEpoch = startEpoch + 86400;

        List<String> stocks = stocksProperties.getSymbols();

        for (String symbol : stocks) {
            YahooFinanceChartDto chartDto = yahooFinanceManager.getHistoricalData(symbol, startEpoch, endEpoch);
            List<OhlcvRecordDTO> dtos = mapToOhlcvRecords(chartDto);
            saveOhlcvRecords(dtos);
        }
    }

    public void fetchAndSaveHistorical() {
        List<String> stocks = stocksProperties.getSymbols();

        Long endpoint = Instant.now().getEpochSecond(); // Current time
        Long startingPoint = 1420099200L; // Thu Jan 01 2015 16:00:00 GMT+0800

        for (String symbol : stocks) {
            YahooFinanceChartDto dto = yahooFinanceManager.getHistoricalData(symbol, startingPoint, endpoint);
            List<OhlcvRecordDTO> records = mapToOhlcvRecords(dto);
            saveOhlcvRecords(records);
        }
    }    

    public void saveOhlcvRecords(List<OhlcvRecordDTO> dtos) {
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

    public List<OhlcvRecordDTO> getOhlcvRecordsFromDb(String symbol, Long startEpoch, Long endEpoch) {
        List<OHLCVEntity> rawRecords = ohlcvRepository.findBySymbolAndTimestampRangeCustom(symbol, startEpoch, endEpoch);

        return rawRecords.stream()
            .map(e -> new OhlcvRecordDTO(
                e.getSymbol(), e.getTimestamp(), e.getOpen(), e.getHigh(), e.getLow(), e.getClose(), e.getVolume()
            ))
            .collect(Collectors.toList());

    }

    @Transactional
    public boolean updateTimestampById(Long id, Long newTimestamp) {
        Optional<OHLCVEntity> optional = ohlcvRepository.findById(id);
        if (optional.isPresent()) {
            OHLCVEntity record = optional.get();
            record.setTimestamp(newTimestamp);
            ohlcvRepository.save(record);
            return true;
        }
        return false;
    }

    public List<OHLCVEntity> getEntitiesBySymbolAndTimestamps(String symbol, List<Long> timestamps) {
        if (timestamps == null || timestamps.isEmpty()) {
            return Collections.emptyList();
        }
        return ohlcvRepository.getEntitiesBySymbolAndTimestamps(symbol, timestamps);
    }

    @Transactional
    public AutoFixResult applyAutoFix(List<OhlcvPatchSuggestion> suggestions) {
        List<OhlcvPatchSuggestion> applied = new ArrayList<>();
        List<OhlcvPatchSuggestion> failed = new ArrayList<>();

        for (OhlcvPatchSuggestion suggestion : suggestions) {
            try {
                int updated = ohlcvRepository.updateTimestampById(suggestion.id(), suggestion.newTimestamp());
                if (updated > 0) {
                    applied.add(suggestion);
                } else {
                    failed.add(suggestion); // not updated (maybe invalid id)
                }
            } catch (Exception e) {
                failed.add(suggestion);
            }
        }
    
        return new AutoFixResult(applied, failed);
    }

    @Override
    public Map<String, Object> getDailyOhlcvData(String symbol) {
        long endTimestamp = Instant.now().getEpochSecond();
        long startTimestamp = endTimestamp - 32L * 24 * 60 * 60; // 32 days of recent data
    
        List<OhlcvRecordDTO> recentData = getOhlcvRecordsFromDb(symbol, startTimestamp, endTimestamp);
        log.info("Recent {} entries fetched from DB for symbol {}", recentData.size(), symbol);
    
        if (recentData.isEmpty()) {
            log.warn("No recent OHLCV data found for symbol {}", symbol);
            return Map.of("candleSticks", Collections.emptyList());
        }
    
        long firstRecentTimestamp = recentData.get(0).getTimestamp();
        List<OHLCVEntity> historyEntities = ohlcvRepository.findPreviousEntries(symbol, firstRecentTimestamp, 149);
        Collections.reverse(historyEntities);
        List<OhlcvRecordDTO> historicalData = historyEntities.stream()
            .map(e -> new OhlcvRecordDTO(e.getSymbol(), e.getTimestamp(), e.getOpen(), e.getHigh(), e.getLow(), e.getClose(), e.getVolume()))
            .collect(Collectors.toList());
    
        List<OhlcvRecordDTO> fullData = new ArrayList<>(historicalData);
        fullData.addAll(recentData);
    
        if (fullData.size() < 150) {
            log.warn("Insufficient data for SMA150 calculation. Found only {} records for {}", fullData.size(), symbol);
            return Map.of("candleSticks", Collections.emptyList());
        }
    
        List<CandleStick> candleSticks = new ArrayList<>();
        Deque<Double> sma10Window = new LinkedList<>();
        Deque<Double> sma20Window = new LinkedList<>();
        Deque<Double> sma30Window = new LinkedList<>();
        Deque<Double> sma50Window = new LinkedList<>();
        Deque<Double> sma100Window = new LinkedList<>();
        Deque<Double> sma150Window = new LinkedList<>();
    
        double sum10 = 0, sum20 = 0, sum30 = 0, sum50 = 0, sum100 = 0, sum150 = 0;
    
        for (int i = 0; i < fullData.size(); i++) {
            OhlcvRecordDTO dto = fullData.get(i);
            double close = dto.getClose();
    
            // Maintain SMA150 window
            sma150Window.addLast(close);
            sum150 += close;
            if (sma150Window.size() > 150) sum150 -= sma150Window.removeFirst();
    
            // SMA100
            sma100Window.addLast(close);
            sum100 += close;
            if (sma100Window.size() > 100) sum100 -= sma100Window.removeFirst();
    
            // SMA50
            sma50Window.addLast(close);
            sum50 += close;
            if (sma50Window.size() > 50) sum50 -= sma50Window.removeFirst();
    
            // SMA30
            sma30Window.addLast(close);
            sum30 += close;
            if (sma30Window.size() > 30) sum30 -= sma30Window.removeFirst();
    
            // SMA20
            sma20Window.addLast(close);
            sum20 += close;
            if (sma20Window.size() > 20) sum20 -= sma20Window.removeFirst();
    
            // SMA10
            sma10Window.addLast(close);
            sum10 += close;
            if (sma10Window.size() > 10) sum10 -= sma10Window.removeFirst();
    
            if (i >= 149) {
                CandleStick stick = new CandleStick(
                    dto.getTimestamp(),
                    dto.getOpen(), dto.getHigh(), dto.getLow(),
                    dto.getClose(), dto.getVolume(),
                    sma10Window.size() == 10 ? sum10 / 10 : null,
                    sma20Window.size() == 20 ? sum20 / 20 : null,
                    sma30Window.size() == 30 ? sum30 / 30 : null,
                    sma50Window.size() == 50 ? sum50 / 50 : null,
                    sma100Window.size() == 100 ? sum100 / 100 : null,
                    sma150Window.size() == 150 ? sum150 / 150 : null
                );
                candleSticks.add(stick);
            }
        }
    
        log.info("Returning {} candlestick entries for {}", candleSticks.size(), symbol);
        return Map.of("candleSticks", candleSticks);
    }

    public Map<String, Object> getWeeklyOhlcvData(String symbol) {
        int maxSmaWeeks = 100;
        int targetWeeks = 54;
        int bufferWeeks = 10;
        int totalWeeksNeeded = maxSmaWeeks + targetWeeks + bufferWeeks; // = 164
        long end = Instant.now().getEpochSecond();
        long start = end - 3600L * 24 * 7 * totalWeeksNeeded;
    
        List<OhlcvRecordDTO> dailyData = getOhlcvRecordsFromDb(symbol, start, end);
        if (dailyData.isEmpty()) return Map.of("candleSticks", List.of());
    
        // Group by ISO week
        WeekFields weekFields = WeekFields.ISO;
        Map<String, List<OhlcvRecordDTO>> grouped = dailyData.stream()
            .collect(Collectors.groupingBy(data -> {
                LocalDate date = Instant.ofEpochSecond(data.getTimestamp()).atZone(ZoneOffset.UTC).toLocalDate();
                int week = date.get(weekFields.weekOfWeekBasedYear());
                int year = date.get(weekFields.weekBasedYear());
                return year + "-W" + String.format("%02d", week);
            }));
    
        // Convert grouped data into weekly candles
        List<CandleStick> weeklyCandles = grouped.entrySet().stream()
            .map(entry -> {
                List<OhlcvRecordDTO> weekData = entry.getValue();
                weekData.sort(Comparator.comparingLong(OhlcvRecordDTO::getTimestamp)); // sorted in chronological order
                double open = weekData.get(0).getOpen();
                double close = weekData.get(weekData.size() - 1).getClose();
                double high = weekData.stream().mapToDouble(OhlcvRecordDTO::getHigh).max().orElse(0);
                double low = weekData.stream().mapToDouble(OhlcvRecordDTO::getLow).min().orElse(0);
                long volume = weekData.stream().mapToLong(OhlcvRecordDTO::getVolume).sum();
                LocalDate monday = Instant.ofEpochSecond(weekData.get(0).getTimestamp()).atZone(ZoneOffset.UTC).toLocalDate().with(weekFields.dayOfWeek(), 1);
                long timestamp = monday.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
                return new CandleStick(timestamp, open, high, low, close, volume, null, null, null, null, null, null);
            })
            .sorted(Comparator.comparingLong(CandleStick::getTimestamp))
            .collect(Collectors.toList());
        
        // Sliding window SMA
        LinkedList<Double> window = new LinkedList<>();
        for (CandleStick candle : weeklyCandles) { // weeklyCandles in chronological order
            window.addFirst(candle.getClose()); // reverse the order by keeping inserting the newer candle at the beginning
            if (window.size() > 100) window.removeLast(); // remove the oldest one if the size exceeds 100
    
            if (window.size() >= 10) candle.setSma10(average(window.subList(0, 10)));
            if (window.size() >= 20) candle.setSma20(average(window.subList(0, 20)));
            if (window.size() >= 30) candle.setSma30(average(window.subList(0, 30)));
            if (window.size() >= 50) candle.setSma50(average(window.subList(0, 50)));
            if (window.size() >= 100) candle.setSma100(average(window.subList(0, 100)));
        }
    
        List<CandleStick> result = weeklyCandles.subList(Math.max(0, weeklyCandles.size() - 54), weeklyCandles.size()); // trim to the latest 52 weekly candles
        return Map.of("candleSticks", result);
    }

    public Map<String, Object> getMonthlyOhlcvData(String symbol) {
        int maxSmaMonths = 30;
        int targetMonths = 60;
        int bufferMonths = 10;
        int totalMonthsNeeded = maxSmaMonths + targetMonths + bufferMonths;
    
        long end = Instant.now().getEpochSecond();
        long start = end - 3600L * 24 * 30L * totalMonthsNeeded;
    
        List<OhlcvRecordDTO> dailyData = getOhlcvRecordsFromDb(symbol, start, end);
        if (dailyData.isEmpty()) return Map.of("candleSticks", List.of());
    
        Map<String, List<OhlcvRecordDTO>> grouped = dailyData.stream()
            .collect(Collectors.groupingBy(data -> {
                LocalDate date = Instant.ofEpochSecond(data.getTimestamp()).atZone(ZoneOffset.UTC).toLocalDate();
                return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            }));
        
        // Group by Year-Month
        List<CandleStick> monthlyCandles = grouped.entrySet().stream()
            .map(entry -> {
                List<OhlcvRecordDTO> monthData = entry.getValue();
                monthData.sort(Comparator.comparingLong(OhlcvRecordDTO::getTimestamp));
                double open = monthData.get(0).getOpen();
                double close = monthData.get(monthData.size() - 1).getClose();
                double high = monthData.stream().mapToDouble(OhlcvRecordDTO::getHigh).max().orElse(0);
                double low = monthData.stream().mapToDouble(OhlcvRecordDTO::getLow).min().orElse(0);
                long volume = monthData.stream().mapToLong(OhlcvRecordDTO::getVolume).sum();
                LocalDate firstDay = Instant.ofEpochSecond(monthData.get(0).getTimestamp()).atZone(ZoneOffset.UTC).toLocalDate().withDayOfMonth(1);
                long timestamp = firstDay.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
                return new CandleStick(timestamp, open, high, low, close, volume, null, null, null, null, null, null);
            })
            .sorted(Comparator.comparingLong(CandleStick::getTimestamp))
            .collect(Collectors.toList());
    
        // Compute SMA values using sliding window
        LinkedList<Double> window = new LinkedList<>();
        for (CandleStick candle : monthlyCandles) {
            window.addFirst(candle.getClose());
            if (window.size() > 30) window.removeLast();
    
            if (window.size() >= 10) candle.setSma10(average(window.subList(0, 10)));
            if (window.size() >= 20) candle.setSma20(average(window.subList(0, 20)));
            if (window.size() >= 30) candle.setSma30(average(window.subList(0, 30)));
        }
    
        // Return the most recent 61 months
        List<CandleStick> result = monthlyCandles.subList(Math.max(0, monthlyCandles.size() - 61), monthlyCandles.size());
        return Map.of("candleSticks", result);
    }

    private double average(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    // @PostConstruct
    // public void logDataSourceInfo() throws SQLException {
    //     var connection = dataSource.getConnection();
    //     System.out.println(">>> Connected DB URL: " + connection.getMetaData().getURL());
    //     System.out.println(">>> Connected DB User: " + connection.getMetaData().getUserName());
    // }
}
