package com.demo.sb.demo_sb_goodbye.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodByeController {
    @GetMapping(value = "/ipad/goodbye")
    public String goodbye() {
        return "Goodbye!";
    }
}
