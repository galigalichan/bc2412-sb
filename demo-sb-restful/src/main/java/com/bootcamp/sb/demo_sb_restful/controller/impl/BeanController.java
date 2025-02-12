package com.bootcamp.sb.demo_sb_restful.controller.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_restful.DemoSbRestfulApplication;

@RestController
public class BeanController {
    @GetMapping(value = "/beans")
    public List<String> getBeans() {
        return Arrays.asList(DemoSbRestfulApplication.context.getBeanDefinitionNames());
    }
    
}
