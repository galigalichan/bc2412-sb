// package com.bootcamp.sb.demo_sb_bc_forum.controller.impl;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.RestController;

// import com.bootcamp.sb.demo_sb_bc_forum.codewave.UserNotFoundException;
// import com.bootcamp.sb.demo_sb_bc_forum.controller.UserOperation;
// import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
// import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
// import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
// import com.bootcamp.sb.demo_sb_bc_forum.dto2.UserCommentDTO;
// import com.bootcamp.sb.demo_sb_bc_forum.dto2.UserDTO;
// import com.bootcamp.sb.demo_sb_bc_forum.dto2.UserDTO.PostDTO;
// import com.bootcamp.sb.demo_sb_bc_forum.dto2.UserDTO.PostDTO.CommentDTO;
// import com.bootcamp.sb.demo_sb_bc_forum.service.JPHService;

// @RestController
// public class UserController2 implements UserOperation {
//   @Autowired
//   private JPHService jphService;

//   @Override
//   public List<UserDTO> getJPHUsers() {
//     List<UserDto> userDtos = this.jphService.getJPHUsers();
//     List<PostDto> postDtos = this.jphService.getJPHPosts();
//     List<CommentDto> commentDtos = this.jphService.getJPHComments();

//     // Null check on each returned objects from JsonPlaceHolder
//     return userDtos.stream().map(u -> {
//       List<PostDTO> postDTOs =
//           postDtos.stream().filter(p -> p.getUserId() == u.getId()).map(p -> {
//             List<CommentDTO> commentDTOs =
//                 commentDtos.stream().filter(c -> c.getPostId() == p.getId())
//                     .map(c -> new CommentDTO(c.getId(), c.getName(),
//                         c.getEmail(), c.getBody()))
//                     .collect(Collectors.toList());
//             return PostDTO.builder().id(p.getId()).title(p.getTitle())
//                 .body(p.getBody()).commentDTOs(commentDTOs).build();
//           }).collect(Collectors.toList());
//       UserDTO.AddressDTO.GeoDTO geoDTO = UserDTO.AddressDTO.GeoDTO.builder()
//           .latitude(u.getAddress().getGeo().getLat())
//           .longitude(u.getAddress().getGeo().getLng()).build();
//       UserDTO.AddressDTO addressDTO = UserDTO.AddressDTO.builder()
//           .city(u.getAddress().getCity()).street(u.getAddress().getStreet())
//           .zipcode(u.getAddress().getZipcode()).suite(u.getAddress().getSuite())
//           .geo(geoDTO).build();
//       UserDTO.CompanyDTO companyDTO =
//           UserDTO.CompanyDTO.builder().bs(u.getCompany().getBs())
//               .catchPhrase(u.getCompany().getCatchPhrase())
//               .name(u.getCompany().getName()).build();
//       return UserDTO.builder().id(u.getId()).name(u.getName())
//           .username(u.getUsername()).email(u.getEmail()).phone(u.getPhone())
//           .website(u.getWebsite()).address(addressDTO).company(companyDTO)
//           .postDTOs(postDTOs).build();
//     }).collect(Collectors.toList());
//   }

//   @Override
//   public List<UserCommentDTO> getUserComments(String userId) {
//     Long uid = Long.valueOf(userId);
//     List<UserDto> userDtos = this.jphService.getJPHUsers();
//     // Check if User exists
//       userDtos.stream() //
//         .filter(u -> u.getId() == uid) //
//         .findAny().orElseThrow(() -> new UserNotFoundException());

//     List<PostDto> postDtos = this.jphService.getJPHPosts();
//     List<CommentDto> commentDtos = this.jphService.getJPHComments();

//     List<UserCommentDTO.CommentDTO> commentDTOs = new ArrayList<>();
//     for (PostDto postDto : postDtos) {
//       if (postDto.getUserId() == uid) {
//         for (CommentDto commentDto : commentDtos) {
//           if (commentDto.getPostId() == postDto.getId()) {
//             commentDTOs.add(new UserCommentDTO.CommentDTO(commentDto.getName(),
//                 commentDto.getEmail(), commentDto.getBody()));
//           }
//         }
//       }
//     }
//     return userDtos.stream()
//       .filter(u -> u.getId() == uid)
//       .map(u -> new UserCommentDTO(u.getId(), u.getUsername(), commentDTOs))
//       .collect(Collectors.toList());
//   }
// }