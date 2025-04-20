package com.bootcamp.bc_xfin_service.service;

import java.util.List;
import java.util.Map;

import com.bootcamp.bc_xfin_service.entity.OHLCVEntity;
import com.bootcamp.bc_xfin_service.model.dto.OhlcvRecordDTO;
import com.bootcamp.bc_xfin_service.validation.AutoFixResult;
import com.bootcamp.bc_xfin_service.validation.OhlcvPatchSuggestion;

public interface OHLCVService {
    public void fetchAndSaveDailyOHLCV();

    public void fetchAndSaveHistorical();

    public List<OhlcvRecordDTO> getOhlcvRecordsFromDb(String symbol, Long startEpoch, Long endEpoch);

    public boolean updateTimestampById(Long id, Long newTimestamp);

    public List<OHLCVEntity> getEntitiesBySymbolAndTimestamps(String symbol, List<Long> timestamps);

    public AutoFixResult applyAutoFix(List<OhlcvPatchSuggestion> suggestions);

    public Map<String, Object> getDailyOhlcvData(String symbol);

    public Map<String, Object> getWeeklyOhlcvData(String symbol);

    public Map<String, Object> getMonthlyOhlcvData(String symbol);
}
