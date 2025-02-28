package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.PostEntity;

public interface PostOperation {
    @GetMapping(value = "/jph/posts")
    List<PostDto> getPostsFromUrl();

    @GetMapping(value = "/api/posts")
    ApiResp<List<PostDto>> getPosts();

    @GetMapping(value = "/api/posts/{useid}")
    ApiResp<List<PostDto>> getPostsByUserId(@PathVariable(value = "userid") String id);

    @PostMapping(value = "/api/post/{userid}")
    ApiResp<PostDto> addPost(@PathVariable(value = "userid") String id, @RequestBody PostDto postDto);
    
    @DeleteMapping(value = "/api/post/{postid}")
    ApiResp<PostEntity> deletePost(@PathVariable(value = "postid") String id);

}
