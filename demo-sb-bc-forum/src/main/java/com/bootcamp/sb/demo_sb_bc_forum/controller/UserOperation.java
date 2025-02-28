package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;

public interface UserOperation {
    @GetMapping(value = "/jph/users")
    List<UserDto> getUsersFromUrl();
    
    @GetMapping(value = "/api/users")
    ApiResp<List<UserEntity>> getUsers();
    
    @GetMapping(value = "/api/user")
    ApiResp<UserEntity> getUserById(@RequestParam(value = "userid") String id);
    
    @PutMapping(value = "/api/user/{userid}")
    ApiResp<UserEntity> replaceUser(@PathVariable(value = "userid") Long id, @RequestBody UserDto newUser);

}
