package com.bootcamp.sb.demo_sb_bccalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
public class CalculatorController {
    @Autowired
    CalculatorService calculatorService;

    @GetMapping(value = "/operation/{operation}")
    public String operate(@RequestParam String x, @RequestParam String y, @PathVariable String operation) {
        return this.calculatorService.operate(x, y, operation);
    }
}
