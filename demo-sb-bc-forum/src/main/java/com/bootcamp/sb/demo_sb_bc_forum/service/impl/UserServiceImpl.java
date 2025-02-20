package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.UserDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<UserDto> getUsers() {
        String url = "https://jsonplaceholder.typicode.com/users";
        
        List<UserDto> userDtos = 
        Arrays.asList(this.restTemplate.getForObject(url, UserDto[].class));

        List<UserDto> filteredUserDtos = userDtos.stream().map(e -> UserDtoMapper.map(e)).collect(Collectors.toList());
    
        return filteredUserDtos;
    }

}
