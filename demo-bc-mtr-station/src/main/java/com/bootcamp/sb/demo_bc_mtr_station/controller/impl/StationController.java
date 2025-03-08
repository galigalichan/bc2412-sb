package com.bootcamp.sb.demo_bc_mtr_station.controller.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.controller.StationOperation;
import com.bootcamp.sb.demo_bc_mtr_station.dto.NextTrainDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.StationDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.service.impl.StationServiceImpl;

@RestController
public class StationController implements StationOperation {
    @Autowired
    private StationServiceImpl stationServiceImpl;

    @PostMapping(value = "/POST/station")
    public ApiResp<StationDto> addStation(@RequestParam(value = "lineid") String lineId, @RequestBody StationDto stationDto) {
        return this.stationServiceImpl.addStation(lineId, stationDto);
    }

    @GetMapping(value = "/GET/station/id/{id}")
    public ApiResp<StationDto> getStationById(@PathVariable String id) {
        return this.stationServiceImpl.getStationById(id);
    }

    @GetMapping(value = "/GET/station/code/{code}")
    public ApiResp<List<StationDto>> getStationByCode(@PathVariable(value = "code") String stationCode) {
        return this.stationServiceImpl.getStationByCode(stationCode);
    }

    @GetMapping(value = "/GET/stations")
    public ApiResp<Map<LineCode, List<StationDto>>> getAllStations() {
        return this.stationServiceImpl.getAllStations();
    }

    @GetMapping(value = "/GET/stations/line")
    public ApiResp<List<StationDto>> getStationsByLine(@RequestParam(value = "linecode") String lineCode) {
        return this.stationServiceImpl.getStationsByLine(lineCode);
    }

    @GetMapping(value = "/GET/nexttrain")
    public NextTrainDto getNextTrain(@RequestParam(value = "line") String lineCode, 
                                    @RequestParam(value = "station") String stationCode) {
        return this.stationServiceImpl.getNextTrain(lineCode, stationCode);
    }

}
