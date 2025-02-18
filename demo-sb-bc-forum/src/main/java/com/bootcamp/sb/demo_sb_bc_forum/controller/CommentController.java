package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.CommentDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.service.CommentService;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/jsonplaceholder/comments")
    public List<CommentDto> getComments() {
        return this.commentService.getComments().stream().map(e -> CommentDtoMapper.map(e)).collect(Collectors.toList());
    }
}
