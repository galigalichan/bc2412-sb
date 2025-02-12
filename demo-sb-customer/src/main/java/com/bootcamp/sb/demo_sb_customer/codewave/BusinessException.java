package com.bootcamp.sb.demo_sb_customer.codewave;

public class BusinessException extends RuntimeException {
    private SysCode sysCode;

    public static BusinessException of(SysCode sysCode) {
        return new BusinessException(sysCode);
    }

    private BusinessException(SysCode sysCode) {
        super(sysCode.getMessage());
        this.sysCode = sysCode;
    }
    
    public SysCode getSysCode() {
        return this.sysCode;
    }
}
