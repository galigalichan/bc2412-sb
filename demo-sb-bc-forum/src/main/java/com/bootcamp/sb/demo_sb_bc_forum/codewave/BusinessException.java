package com.bootcamp.sb.demo_sb_bc_forum.codewave;

public class BusinessException extends RuntimeException {
    private final SysCode sysCode;

    // public static BusinessException of(SysCode sysCode) {
    //     return new BusinessException(sysCode);
    // }

    public BusinessException(SysCode sysCode) {
        super(sysCode.getMessage());
        this.sysCode = sysCode;
    }
    
    public SysCode getSysCode() {
        return this.sysCode;
    }
}

