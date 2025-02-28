package com.bootcamp.sb.demo_sb_bccalculator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.sb.demo_sb_bccalculator.dto.CalRespDTO;

// Do it on operation first. 
public interface CalculatorOperation {
    @GetMapping(value = "/operation")
    CalRespDTO calculate1(@RequestParam String x, @RequestParam String y, @RequestParam String operation);

    @GetMapping(value = "/operation/{x}/{y}/{operation}")
    CalRespDTO calculate2(@PathVariable String x, @PathVariable String y, @PathVariable String operation);

    @PostMapping(value = "/operation")
    CalRespDTO calculate3(@RequestBody CalRespDTO calRespDTO);

    
}
