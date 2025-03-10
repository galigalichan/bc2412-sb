package com.bootcamp.sb.final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.final_project.service.CrumbManager;

@RestController
public class CrumbController {
    @Autowired
    private CrumbManager crumbManager;

    @GetMapping(value = "/crumb")
    public String getCrumb() {
        return this.crumbManager.getCrumb();
    }
}
