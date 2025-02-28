// package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

// import java.util.Arrays;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
// import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
// import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
// import com.bootcamp.sb.demo_sb_bc_forum.lib.XUriBuilder;
// import com.bootcamp.sb.demo_sb_bc_forum.service.JPHService;

// @Service
// public class JPHServiceImpl implements JPHService {
//   @Autowired
//   private RestTemplate restTemplate;

//   @Value("${api.jph.host}")
//   private String host;

//   @Value("${api.jph.endpoints.users}")
//   private String userEndpoint;

//   @Value("${api.jph.endpoints.posts}")
//   private String postEndpoint;

//   @Value("${api.jph.endpoints.comments}")
//   private String commentEndpoint;

//   @Override
//   public List<UserDto> getJPHUsers() {
//     String url = XUriBuilder.create() //
//         .https() //
//         .host(host) //
//         .path(userEndpoint) //
//         .build();
//     return Arrays.asList(this.restTemplate.getForObject(url, UserDto[].class));
//   }

//   @Override
//   public List<PostDto> getJPHPosts() {
//     String url = XUriBuilder.create() //
//         .https() //
//         .host(host) //
//         .path(postEndpoint) //
//         .build();
//     return Arrays.asList(this.restTemplate.getForObject(url, PostDto[].class));
//   }

//   @Override
//   public List<CommentDto> getJPHComments() {
//     String url = XUriBuilder.create() //
//         .https() //
//         .host(host) //
//         .path(commentEndpoint) //
//         .build();
//     return Arrays
//         .asList(this.restTemplate.getForObject(url, CommentDto[].class));
//   }
// }