package com.bootcamp.sb.demo_sb_bc_forum.dto.mapper;

import org.springframework.stereotype.Component;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;

@Component
public class UserDtoMapper {
    public static UserDto map(UserDto e) {
        UserDto.Address.Geo userAddressGeo = UserDto.Address.Geo.builder()
            .lat(e.getAddress().getGeo().getLat())
            .lng(e.getAddress().getGeo().getLng())
            .build();

        UserDto.Address userAddress = UserDto.Address.builder()
            .street(e.getAddress().getStreet())
            .suite(e.getAddress().getSuite())
            .city(e.getAddress().getCity())
            .zipcode(e.getAddress().getZipcode())
            .geo(userAddressGeo)
            .build();

        return UserDto.builder()
            .id(e.getId())
            .name(e.getName())
            .username(e.getUsername())
            .email(e.getEmail())
            .address(userAddress)
            .phone(e.getPhone())
            .website(e.getWebsite())
            .company(e.getCompany())
            .build();
    }
}
