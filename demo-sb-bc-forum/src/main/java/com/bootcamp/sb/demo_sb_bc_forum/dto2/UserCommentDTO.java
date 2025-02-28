package com.bootcamp.sb.demo_sb_bc_forum.dto2;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCommentDTO {
  private Long id;
  private String username;
  @JsonProperty(value = "comments")
  private List<CommentDTO> commentDTOs;

  @Getter
  @AllArgsConstructor
  public static class CommentDTO {
    private String name;
    private String email;
    private String body;
  }
}