package com.bootcamp.sb.demo_bc_mtr_station.service;

import java.util.List;
import java.util.Map;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.dto.NextTrainDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.StationDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;

public interface StationService {
    ApiResp<StationDto> addStation(String id, StationDto stationDto);

    ApiResp<StationDto> getStationById(String id);

    ApiResp<List<StationDto>> getStationByCode(String stationCode);

    ApiResp<Map<LineCode, List<StationDto>>> getAllStations();

    ApiResp<List<StationDto>> getStationsByLine(String lineCode);

    NextTrainDto getNextTrain(String line, String station);
}

