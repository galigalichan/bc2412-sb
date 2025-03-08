package com.bootcamp.sb.demo_bc_mtr_station.service;

import java.util.List;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineSignalDto;

public interface LineService {
    ApiResp<LineDto> addLine(LineDto lineDto);

    LineSignalDto getLineSignal(String line);

    List<LineSignalDto> getAllLineSignals();

}
