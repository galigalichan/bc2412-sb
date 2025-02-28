package com.bootcamp.sb.demo_sb_customer.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_customer.dto.UserDTO;
import com.bootcamp.sb.demo_sb_customer.dto.mapper.UserDTOMapper;
import com.bootcamp.sb.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.sb.demo_sb_customer.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @GetMapping(value = "/jsonplaceholder/users")
    // public List<UserDto> getUsers() {
    public List<UserDTO> getUsers() throws JsonProcessingException {
        // return this.userService.getUsers();
        // List of UserDto -> List of UserDTO
        List<UserDto> userDtos = this.userService.getUsers();
        System.out.println("userDtos=" + userDtos);
        // Stream method with mapper
        return this.userService.getUsers().stream().map(e -> userDTOMapper.map(e)).collect(Collectors.toList());

        // Stream method without mapper
        // return this.userService.getUsers().stream().map(e -> {
        //     UserDTO.Address.Geo geo = UserDTO.Address.Geo.builder().latitude(e.getAddress().getGeo().getLatitude()).longitude(e.getAddress().getGeo().getLongitude()).build();
        //     UserDTO.Address address = UserDTO.Address.builder().street(e.getAddress().getStreet()).suite(e.getAddress().getSuite()).city(e.getAddress().getCity()).zipcode(e.getAddress().getZipcode()).geo(geo).build();
        //         return UserDTO.builder()
        //             .id(e.getId())
        //             .name(e.getName())
        //             .username(e.getUsername())
        //             .email(e.getEmail())
        //             .address(address)
        //             .build();
        // }).collect(Collectors.toList());

        // For loop method with mapper
        // List<UserDto> serviceResults = this.userService.getUsers();
        // List<UserDTO> userDTOs = new ArrayList<>();
        // for (UserDto dto : serviceResults) {
        //     UserDTO userDTO = UserDTOMapper.map(dto);
        //     userDTOs.add(userDTO);
        //     }
        //         return userDTOs;
        
        
    }
}
