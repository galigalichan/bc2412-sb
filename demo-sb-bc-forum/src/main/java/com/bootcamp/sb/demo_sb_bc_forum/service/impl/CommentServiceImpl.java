package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.CommentDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<CommentDto> getComments() {
        String url = "https://jsonplaceholder.typicode.com/comments";

        List<CommentDto> commentDtos = 
            Arrays.asList(this.restTemplate.getForObject(url, CommentDto[].class));

        List<CommentDto> filteredCommentDtos = commentDtos.stream().map(e -> CommentDtoMapper.map(e)).collect(Collectors.toList());

        return filteredCommentDtos;
    }
}
