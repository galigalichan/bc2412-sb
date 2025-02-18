package com.bootcamp.sb.demo_sb_bc_forum.dto;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto.Post;

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
public class CombinedDto2 {
    private Long id;
    private String username;
    // private List<Post> posts;
    private List<Post.Comment> comments;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        private List<Comment> comments;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Comment {
            private String name;
            private String email;
            private String body;
        }
    }
}
