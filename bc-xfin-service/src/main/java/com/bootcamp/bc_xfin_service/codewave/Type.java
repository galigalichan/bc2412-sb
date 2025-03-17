package com.bootcamp.bc_xfin_service.codewave;

public enum Type {
    FIVEM("5 Minutes"),
    D("Daily"),
    W("Weekly"),
    M("Monthly"),
    ;

    private final String description;

    private Type(String description) {
        this.description = description;
    }


    public String getDescription() {
        return this.description;
    }

}
