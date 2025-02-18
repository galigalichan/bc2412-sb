package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.PostDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.service.PostService;

@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping(value = "/jsonplaceholder/posts")
    public List<PostDto> getPosts() {
        return this.postService.getPosts().stream().map(e -> PostDtoMapper.map(e)).collect(Collectors.toList());
    }
}
