package com.bootcamp.sb.demo_sb_customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bootcamp.sb.demo_sb_customer.dto.mapper.CalculatorResult;

public interface CalculatorOperation {
    @GetMapping(value = "/calculate/{x}/{y}")
    public CalculatorResult calculate(@PathVariable int x, @PathVariable int y);
    

    // 1 + 2 -> 3
}
