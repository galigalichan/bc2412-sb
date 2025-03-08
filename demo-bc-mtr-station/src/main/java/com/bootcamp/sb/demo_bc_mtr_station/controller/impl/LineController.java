package com.bootcamp.sb.demo_bc_mtr_station.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.controller.LineOperation;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineSignalDto;
import com.bootcamp.sb.demo_bc_mtr_station.service.impl.LineServiceImpl;

@RestController
public class LineController implements LineOperation {
    @Autowired
    private LineServiceImpl lineServiceImpl;

    @PostMapping(value = "/POST/line")
    public ApiResp<LineDto> addLine(@RequestBody LineDto lineDto) {
        return this.lineServiceImpl.addLine(lineDto);
    }

    @GetMapping(value = "/line/signal")
    public LineSignalDto getLineSignal(@RequestParam(value = "line") String lineCode) {
        return this.lineServiceImpl.getLineSignal(lineCode);
    }

    @GetMapping(value = "line/signals")
    public List<LineSignalDto> getAllLineSignals() {
        return this.lineServiceImpl.getAllLineSignals();
    }

}
