package com.bootcamp.sb.demo_sb_bccalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    public String operate(String x, String y, String operation) {
        try {
            Double.parseDouble(x);
        } catch (NumberFormatException e) {
            throw new BusinessException("Invalid input of x.");
        }

        try {
            Double.parseDouble(y);
        } catch (NumberFormatException e) {
            throw new BusinessException("Invalid input of y.");
        }

        return switch(operation) {
            case "add" -> add(x, y);
            case "sub" -> subtract(x, y);
            case "mul" -> multiply(x, y);
            case "div" -> divide(x, y);
            default -> throw new BusinessException("Invalid input of operation.");
        };
    }

        public String add(String x, String y) {
            Double numX = Double.parseDouble(x);
            Double numY = Double.parseDouble(y);
            return BigDecimal.valueOf(numX).add(BigDecimal.valueOf(numY)).toString();
        }

        public String subtract(String x, String y) {
            Double numX = Double.parseDouble(x);
            Double numY = Double.parseDouble(y);
            return BigDecimal.valueOf(numX).subtract(BigDecimal.valueOf(numY)).toString();
        }

        public String multiply(String x, String y) {
            Double numX = Double.parseDouble(x);
            Double numY = Double.parseDouble(y);
            return BigDecimal.valueOf(numX).multiply(BigDecimal.valueOf(numY)).toString();
        }

        public String divide(String x, String y) {
            Double numX = Double.parseDouble(x);
            Double numY = Double.parseDouble(y);
            if (numY == 0) {
                throw new ArithmeticException("Cannot divide by zero.");
            }
            return BigDecimal.valueOf(numX).divide(BigDecimal.valueOf(numY)).setScale(5,RoundingMode.HALF_UP).toString();   
        }
    
}
