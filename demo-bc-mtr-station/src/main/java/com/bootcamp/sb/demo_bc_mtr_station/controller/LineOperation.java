package com.bootcamp.sb.demo_bc_mtr_station.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineSignalDto;

public interface LineOperation {
    @PostMapping(value = "/POST/line")
    ApiResp<LineDto> addLine(@RequestBody LineDto lineDto);

    @GetMapping(value = "/line/signal")
    LineSignalDto getLineSignal(@RequestParam(value = "line") String lineCode);

    @GetMapping(value = "line/signals")
    List<LineSignalDto> getAllLineSignals();

}
