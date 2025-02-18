package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto2;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.CombinedInfoServiceImpl;

@RestController
public class CombinedInfoController {
    @Autowired
    private CombinedInfoServiceImpl combinedInfoServiceImpl;

    @GetMapping(value = "/jsonplaceholder/combinedinfo")
    public List<CombinedDto> getCombinedInfo() {
        return this.combinedInfoServiceImpl.getCombinedInfo();
    }

    @GetMapping(value = "/jsonplaceholder/allcommentsbyuserid")
    public List<CombinedDto2> getAllCommentsByUserId(@RequestParam(value = "userid") String id) {
        return this.combinedInfoServiceImpl.getAllCommentsByUserId(id);
    }
}
