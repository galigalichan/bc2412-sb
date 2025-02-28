package com.bootcamp.sb.demo_sb_bccalculator.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bccalculator.CalculatorOperation;
import com.bootcamp.sb.demo_sb_bccalculator.dto.CalRespDTO;
import com.bootcamp.sb.demo_sb_bccalculator.dto.mapper.DTOMapper;
import com.bootcamp.sb.demo_sb_bccalculator.model.Operation;

@RestController
public class CalculatorController implements CalculatorOperation{
    // @Autowired
    // CalculatorService calculatorService;

    // @GetMapping(value = "/operation/{operation}")
    // public String operate(@RequestParam String x, @RequestParam String y, @PathVariable String operation) {
    //     return this.calculatorService.operate(x, y, operation);
    // }
    @Autowired
    private DTOMapper mapper;

    @Override
    public CalRespDTO calculate1(String x, String y, String operation) {
        Double param1 = Double.valueOf(x);
        Double param2 = Double.valueOf(y);
        Operation operator = Operation.of(operation);
        Double result = calculate(param1, param2, operator);
        return this.mapper.map(param1, param2, operator, result);
    }

    @Override
    public CalRespDTO calculate2(String x, String y, String operation) {
        Double param1 = Double.valueOf(x);
        Double param2 = Double.valueOf(y);
        Operation operator = Operation.of(operation);
        Double result = calculate(param1, param2, operator);
        return this.mapper.map(param1, param2, operator, result);
    }

    @Override
    public CalRespDTO calculate3(CalRespDTO reqDTO) {
        Double param1 = Double.valueOf(reqDTO.getX());
        Double param2 = Double.valueOf(reqDTO.getY());
        Operation operator = Operation.of(reqDTO.getOperation());
        Double result = calculate(param1, param2, operator);
        return this.mapper.map(param1, param2, operator, result);
    }

    private Double calculate(Double x, Double y, Operation operation) { // 
        return switch (operation) {
            case ADD -> BigDecimal.valueOf(x).add(BigDecimal.valueOf(y)).doubleValue();
            case SUB -> BigDecimal.valueOf(x).subtract(BigDecimal.valueOf(y)).doubleValue();
            case MUL -> BigDecimal.valueOf(x).subtract(BigDecimal.valueOf(y)).doubleValue();
            case DIV -> BigDecimal.valueOf(x).divide(BigDecimal.valueOf(y), 5, RoundingMode.HALF_DOWN).doubleValue(); // TBC
        };
    }

}
