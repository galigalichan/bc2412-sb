package com.bootcamp.sb.demo_sb_bc_forum.controller.impl;

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

    @GetMapping(value = "/jph/combinedinfo")
    public List<CombinedDto> getCombinedInfoFromUrl() {
        return this.combinedInfoServiceImpl.getCombinedInfoFromUrl();
    }

    @GetMapping(value = "/jph/allcommentsbyuserid")
    public List<CombinedDto2> getAllCommentsByUserId(@RequestParam(value = "userid") String id) {
        return this.combinedInfoServiceImpl.getAllCommentsByUserId(id);
    }

}
