package com.bootcamp.sb.demo_bc_mtr_station.exception;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.SysCode;

public class InvalidInputException extends RuntimeException {
    private final SysCode sysCode;

    public InvalidInputException(SysCode sysCode) {
        super(sysCode.getMessage());
        this.sysCode = sysCode;
    }
        
    public SysCode getSysCode() {
        return this.sysCode;
    }
    
}
