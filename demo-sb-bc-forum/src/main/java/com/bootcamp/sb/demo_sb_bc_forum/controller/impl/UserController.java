package com.bootcamp.sb.demo_sb_bc_forum.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.controller.UserOperation;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UserController implements UserOperation {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping(value = "/jph/users")
    public List<UserDto> getUsersFromUrl() {
        return this.userServiceImpl.getUsersFromUrl();
    }

    @GetMapping(value = "/api/users")
    public ApiResp<List<UserEntity>> getUsers() {
        return this.userServiceImpl.getUsers();
    }

    @GetMapping(value = "/api/user")
    public ApiResp<UserEntity> getUserById(@RequestParam(value = "userid") String id) {
        return this.userServiceImpl.getUserById(id);
    }
    
    @PutMapping(value = "/api/user/{userid}")
    public ApiResp<UserEntity> replaceUser(@PathVariable(value = "userid") Long id, @RequestBody UserDto newUser) {
        return this.userServiceImpl.replaceUser(id, newUser);
    }
    
}
