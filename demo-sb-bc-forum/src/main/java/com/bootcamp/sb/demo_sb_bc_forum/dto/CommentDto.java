package com.bootcamp.sb.demo_sb_bc_forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long postId;
    private Long id;
    private String name;
    private String email;
    private String body;

    public CommentDto(Long id, String name, String email, String body, Long postId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
        this.postId = postId;
    }
}
