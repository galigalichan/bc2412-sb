package com.bootcamp.sb.demo_sb_bc_forum.codewave;

import java.util.Collections;
import java.util.List;

public enum SysCode {
    ID_NOT_FOUND("1", "User not found."),

    INVALID_INPUT("2", "Invalid Input."),
    
    RESTTEMPLATE_EXCEPTION("3", "RestTemplate Error - JsonPlaceHolder"),
    
    SUCCESS("000000", "Success."),
    
    API_UNAVAILABLE("999998","Json Placeholder API unavailable.", Collections.<String> emptyList()),
    
    CONNECTION_FAILURE("999997","Database Connection Fail.", Collections.<String> emptyList());

    private final String code;
    private final String message;
    private final List<String> data;

    private SysCode(String code, String message, List<String> data) {
        this.code = code;
        this.message = message;
        this.data = data != null ? data : Collections.emptyList();
    }

    private SysCode(String code, String message) {
        this(code, message, Collections.emptyList());

    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public List<String> getData() {
        return this.data;
    }


}
