package com.bootcamp.sb.demo_sb_customer.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp.sb.demo_sb_customer.service.CalculatorService;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public int sum(int x, int y) {
        return x + y;
    }

    @Override
    public int subtract(int x, int y) {
        return x - y;
    }
}
