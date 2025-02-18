package com.bootcamp.sb.demo_sb_bc_forum.exception;

public enum SysCode {
    ID_NOT_FOUND("1", "User not found."),

    INVALID_INPUT("2", "Invalid Input."),
    
    RESTTEMPLATE_EXCEPTION("3", "RestTemplate Error - JsonPlaceHolder"),;

    private final String code;
    private final String message;

    private SysCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }


}
