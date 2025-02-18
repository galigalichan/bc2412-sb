package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.service.PostService;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PostDto> getPosts() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        List<PostDto> postDtos = 
            Arrays.asList(this.restTemplate.getForObject(url, PostDto[].class));

        return postDtos;
    }
}
