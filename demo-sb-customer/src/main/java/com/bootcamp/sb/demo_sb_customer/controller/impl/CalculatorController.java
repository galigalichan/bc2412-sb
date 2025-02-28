package com.bootcamp.sb.demo_sb_customer.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_customer.controller.CalculatorOperation;
import com.bootcamp.sb.demo_sb_customer.dto.mapper.CalculatorResult;
import com.bootcamp.sb.demo_sb_customer.service.CalculatorService;

// @Autowired(required=false)
// CalculatorOperation calculatorOperation
// then method will call calculationOperation.xxxx()

@RestController
public class CalculatorController implements CalculatorOperation {
    @Autowired
    private CalculatorService calculatorService;

    @Override
    // Unit test for calculate: assume sum() return 100 and subtract 200 -> -100
    public CalculatorResult calculate(int x, int y) {
        return new CalculatorResult(calculatorService.sum(x, y) - calculatorService.subtract(x, y));
    }

    // Unit test for sum()
    // 1 + 2 -> 3
    // private int sum(int x, int y) {
    //     return x + y;
    // }

    // Unit test for subtract
    // 1 - 2 -> -1
    // private int subtract(int x, int y) {
    //     return x - y;
    // }
}
