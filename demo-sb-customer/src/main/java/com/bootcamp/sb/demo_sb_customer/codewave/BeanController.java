package com.bootcamp.sb.demo_sb_customer.codewave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class BeanController {
    @Autowired
    private String abc;

    @GetMapping(value = "/bean/tutor")
    public String getTutor() {
        return abc; // "Vincent"

    }
}
