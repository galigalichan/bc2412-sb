package com.bootcamp.sb.demo_sb_customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bootcamp.sb.demo_sb_customer.service.CalculatorService;

@SpringBootTest // real life cycle
public class CalculatorServiceTest {
    @Autowired
    private CalculatorService calculatorService;

    // No need to mock Bean since CalculatorService does not have dependency on others.

    // @Test
    void testSum() {
        assertEquals(3, calculatorService.sum(1,2));
    }

    // @Test
    void testSubtract() {
        assertEquals(-1, calculatorService.subtract(1,2));
    }
}
