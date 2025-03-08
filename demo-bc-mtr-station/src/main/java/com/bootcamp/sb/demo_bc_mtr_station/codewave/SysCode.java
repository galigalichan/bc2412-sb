package com.bootcamp.sb.demo_bc_mtr_station.codewave;

import java.util.Collections;
import java.util.List;

public enum SysCode {
    SUCCESS("200", "Success."),

    LINE_ADD_SUCCESS("200", "The line has been added successfully."),

    STATION_ADD_SUCCESS("200", "The station has been added successfully."),

    LINE_NOT_FOUND("404", "Line not found."),

    STATION_NOT_FOUND("404", "Station not found."),

    INVALID_INPUT("400", "Invalid Input."),;  

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
