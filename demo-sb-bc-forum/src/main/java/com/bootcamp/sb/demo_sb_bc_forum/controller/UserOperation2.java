package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.sb.demo_sb_bc_forum.dto2.UserCommentDTO;
import com.bootcamp.sb.demo_sb_bc_forum.dto2.UserDTO;

public interface UserOperation2 {
    @GetMapping(value = "/jph/users")
    List<UserDTO> getJPHUsers();
  
    @GetMapping(value = "/jph/comments")
    List<UserCommentDTO> getUserComments(@RequestParam String userId);
  }
