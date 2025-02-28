package com.bootcamp.sb.demo_sb_customer.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.sb.demo_sb_customer.dto.UserDTO;
import com.bootcamp.sb.demo_sb_customer.model.dto.UserDto;

@Component // bean
public class UserDTOMapper {
    // instance method
    public UserDTO map(UserDto dto) {
            // create object
            UserDTO.Address.Geo userAddressGeo = UserDTO.Address.Geo.builder()
                .latitude(dto.getAddress().getGeo().getLatitude())
                .longitude(dto.getAddress().getGeo().getLongitude())
                .build();

            // create object
            UserDTO.Address userAddress = UserDTO.Address.builder()
                .city(dto.getAddress().getCity())
                .street(dto.getAddress().getStreet())
                .suite(dto.getAddress().getSuite())
                .geo(userAddressGeo)
                .build();

           return UserDTO.builder()
                .id(dto.getId())
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .address(userAddress)
                .build();
    }
}
