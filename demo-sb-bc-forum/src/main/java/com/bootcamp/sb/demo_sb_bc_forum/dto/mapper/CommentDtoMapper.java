package com.bootcamp.sb.demo_sb_bc_forum.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;

@Component
public class CommentDtoMapper {
    public static CommentDto map(CommentDto commentDto) {
        return CommentDto.builder()
        .postId(commentDto.getPostId())
        .id(commentDto.getId())
        .name(commentDto.getName())
        .email(commentDto.getEmail())
        .body(commentDto.getBody())
        .build();
    }
}
