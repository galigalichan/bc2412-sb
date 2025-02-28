package com.bootcamp.sb.demo_sb_customer.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_customer.model.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {
    List<UserDto> getUsers() throws JsonProcessingException;
}
