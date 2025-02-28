package com.bootcamp.sb.demo_sb_bc_forum.dto2;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class UserDTO {
  private Long id;
  private String name;
  private String username;
  private String email;
  private AddressDTO address;
  private String phone;
  private String website;
  private CompanyDTO company;
  @JsonProperty(value = "posts")
  private List<PostDTO> postDTOs;

  @Getter
  @Builder
  @AllArgsConstructor
  public static class AddressDTO {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private GeoDTO geo;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GeoDTO {
      @JsonProperty(value = "lat")
      private Double latitude;
      @JsonProperty(value = "lng")
      private Double longitude;
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class CompanyDTO {
    private String name;
    private String catchPhrase;
    private String bs;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @Setter
  public static class PostDTO {
    private Long id;
    private String title;
    private String body;
    @JsonProperty(value = "comments")
    private List<CommentDTO> commentDTOs;

    @Getter
    @Builder
    @AllArgsConstructor
    @Setter
    public static class CommentDTO {
      private Long id;
      private String name;
      private String email;
      private String body;
    }
  }
}
