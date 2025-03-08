package com.bootcamp.sb.demo_bc_mtr_station.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.dto.NextTrainDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.StationDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;

public interface StationOperation {
    @PostMapping(value = "/POST/station")
    ApiResp<StationDto> addStation(@RequestParam String lineId, @RequestBody StationDto stationDto);

    @GetMapping(value = "/GET/station/id")
    ApiResp<StationDto> getStationById(@PathVariable String id);

    @GetMapping(value = "/GET/station/code")
    ApiResp<List<StationDto>> getStationByCode(@PathVariable String stationCode);

    @GetMapping(value = "/GET/stations")
    ApiResp<Map<LineCode, List<StationDto>>> getAllStations();

    @GetMapping(value = "/GET/stations/line")
    ApiResp<List<StationDto>> getStationsByLine(@RequestParam String linecode);

    @GetMapping(value = "/GET/nexttrain")
    NextTrainDto getNextTrain(@RequestParam(value = "line") String lineCode, @RequestParam(value = "station") String stationCode);

}
