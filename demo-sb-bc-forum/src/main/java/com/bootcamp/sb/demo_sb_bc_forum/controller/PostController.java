package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.PostServiceImpl;

@RestController
public class PostController {
    @Autowired
    private PostServiceImpl postServiceImpl;

    @GetMapping(value = "/jsonplaceholder/posts")
    public List<PostDto> getPosts() {
        return this.postServiceImpl.getPosts();
    }
}
