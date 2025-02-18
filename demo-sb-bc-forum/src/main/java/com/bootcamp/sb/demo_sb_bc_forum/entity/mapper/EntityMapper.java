package com.bootcamp.sb.demo_sb_bc_forum.entity.mapper;

import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.AddressEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CompanyEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.GeoEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;

public class EntityMapper {
    public UserEntity map(UserDto dto) {
        return UserEntity.builder()
            .email(dto.getEmail())
            .name(dto.getName())
            .username(dto.getUsername())
            .website(dto.getWebsite())
            .phone(dto.getPhone())
            .build();

    }

    public AddressEntity map(UserDto.Address address) {
        return AddressEntity.builder()
            .street(address.getStreet())
            .suite(address.getSuite())
            .city(address.getCity())
            .zipcode(address.getZipcode())
            .build();
    }

    public CompanyEntity map(UserDto.Company company) {
        return CompanyEntity.builder()
            .catchPhrase(company.getCatchPhrase())
            .bs(company.getBs())
            .name(company.getName())
            .build();
    }

    public GeoEntity map(UserDto.Address.Geo geo) {
        return GeoEntity.builder()
            .lat(Double.valueOf(geo.getLat()))
            .lng(Double.valueOf(geo.getLng()))
            .build();

    }
    
}
