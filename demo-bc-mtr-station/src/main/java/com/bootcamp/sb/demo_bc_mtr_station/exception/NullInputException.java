package com.bootcamp.sb.demo_bc_mtr_station.exception;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.SysCode;

public class NullInputException extends RuntimeException {
    private final SysCode sysCode;

    public NullInputException(SysCode sysCode) {
        super(sysCode.getMessage());
        this.sysCode = sysCode;
    }
        
    public SysCode getSysCode() {
        return this.sysCode;
    }    
}
