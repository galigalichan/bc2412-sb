package com.bootcamp.sb.demo_bc_mtr_station.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_bc_mtr_station.dto.MtrResponseDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.bootcamp.sb.demo_bc_mtr_station.service.impl.MtrResponseImpl;

@RestController
public class MtrResponseController {
    @Autowired
    private MtrResponseImpl mtrResponseImpl;

    // @GetMapping(value = "/GET/nexttrain")
    // public MtrScheduleData getNextTrain(@RequestParam(value = "line") LineCode lineCode, @RequestParam(value = "station") StationCode stationCode) {
    //     return this.mtrResponseImpl.getNextTrain(lineCode, stationCode);
    // }
    
    @GetMapping(value = "/GET/trainschedule")
    public MtrResponseDto getTrainSchedule(@RequestParam(value = "line") LineCode lineCode, 
                                    @RequestParam(value = "station") StationCode stationCode) {
        return this.mtrResponseImpl.getTrainSchedule(lineCode, stationCode);
    }
    
}
