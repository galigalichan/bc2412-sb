package com.bootcamp.sb.demo_sb_bccalculator.model;

import com.bootcamp.sb.demo_sb_bccalculator.exception.OperationArgumentException;

public enum Operation {
    ADD, SUB, MUL, DIV,;

    // String to  enum
    public static Operation of(String value) {
        for (Operation o : values()) {
            if (o.name().equals(value.trim().toUpperCase())) { // in case of case-sensitivity
                return o;
            }
        }
        throw new OperationArgumentException();
    }
}
