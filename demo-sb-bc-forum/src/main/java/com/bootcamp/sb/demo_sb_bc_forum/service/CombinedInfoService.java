package com.bootcamp.sb.demo_sb_bc_forum.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto2;

public interface CombinedInfoService {
    List<CombinedDto> getCombinedInfoFromUrl();

    List<CombinedDto2> getAllCommentsByUserId(String id);
}
