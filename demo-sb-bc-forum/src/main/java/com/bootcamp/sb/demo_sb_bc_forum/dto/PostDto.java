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
public class PostDto {
    private Long userId;
    private Long id;
    private String title;
    private String body;

    public PostDto(Long id, String title, String body, Long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

}
