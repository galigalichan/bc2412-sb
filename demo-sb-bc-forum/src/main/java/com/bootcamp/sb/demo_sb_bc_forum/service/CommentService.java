package com.bootcamp.sb.demo_sb_bc_forum.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;

public interface CommentService {
    List<CommentDto> getComments();
}
