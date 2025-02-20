package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.UserServiceImpl;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping(value = "/jsonplaceholder/users")
    public List<UserDto> getUsers() {
        return this.userServiceImpl.getUsers();
    }
}
