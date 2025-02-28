package com.bootcamp.sb.demo_sb_bc_forum.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;

public interface UserService {
    List<UserDto> getUsersFromUrl();

    ApiResp<List<UserEntity>> getUsers();

    ApiResp<UserEntity> getUserById(String id);

    ApiResp<UserEntity> replaceUser(Long id, UserDto userDto);

}
