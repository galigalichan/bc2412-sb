package com.bootcamp.sb.demo_sb_bc_forum.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.controller.PostOperation;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.PostEntity;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.PostServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PostController implements PostOperation {
    @Autowired
    private PostServiceImpl postServiceImpl;

    @GetMapping(value = "/jph/posts")
    public List<PostDto> getPostsFromUrl() {
        return this.postServiceImpl.getPostsFromUrl();
    }

    @GetMapping(value = "/api/posts")
    public ApiResp<List<PostDto>> getPosts() {
        return this.postServiceImpl.getPosts();
    }

    @GetMapping(value = "/api/posts/{userid}")
    public ApiResp<List<PostDto>> getPostsByUserId(@PathVariable(value = "userid") String id) {
        return this.postServiceImpl.getPostsByUserId(id);
    }

    @PostMapping(value = "/api/post/{userid}")
    public ApiResp<PostDto> addPost(@PathVariable(value = "userid") String id, @RequestBody PostDto postDto) {
        return this.postServiceImpl.addPost(id, postDto);
    }
    
    @DeleteMapping(value = "/api/post/{postid}")
    public ApiResp<PostEntity> deletePost(@PathVariable(value = "postid") String id) {
        return this.postServiceImpl.deletePost(id);
    }
}
