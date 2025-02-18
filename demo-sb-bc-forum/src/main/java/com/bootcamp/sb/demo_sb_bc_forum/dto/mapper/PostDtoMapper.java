package com.bootcamp.sb.demo_sb_bc_forum.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;

@Component
public class PostDtoMapper {
    public static PostDto map(PostDto postDto) {
        return PostDto.builder()
            .userId(postDto.getUserId())
            .id(postDto.getId())
            .title(postDto.getTitle())
            .body(postDto.getBody())
            .build();
    }
}
