package com.bootcamp.sb.demo_sb_bc_forum.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.PostEntity;

public interface PostService {
    List<PostDto> getPostsFromUrl();

    ApiResp<List<PostDto>> getPosts();

    ApiResp<List<PostDto>> getPostsByUserId(String id);

    ApiResp<PostDto> addPost(String id, PostDto postDto);

    ApiResp<PostEntity> deletePost(String id);
}
