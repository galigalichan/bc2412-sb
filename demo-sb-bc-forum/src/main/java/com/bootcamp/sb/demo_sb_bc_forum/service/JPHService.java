package com.bootcamp.sb.demo_sb_bc_forum.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;

public interface JPHService {
    List<UserDto> getJPHUsers();
    List<PostDto> getJPHPosts();
    List<CommentDto> getJPHComments();
    }
