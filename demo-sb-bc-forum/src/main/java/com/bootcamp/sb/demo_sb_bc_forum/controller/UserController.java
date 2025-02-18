package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.UserDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/jsonplaceholder/users")
    public List<UserDto> getUsers() {
        return this.userService.getUsers().stream().map(e -> UserDtoMapper.map(e)).collect(Collectors.toList());
    }
}
