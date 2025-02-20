package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.CommentServiceImpl;

@RestController
public class CommentController {
    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @GetMapping(value = "/jsonplaceholder/comments")
    public List<CommentDto> getComments() {
        return this.commentServiceImpl.getComments();
    }
}
