package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.codewave.BusinessException;
import com.bootcamp.sb.demo_sb_bc_forum.codewave.GlobalExceptionHandler;
import com.bootcamp.sb.demo_sb_bc_forum.codewave.SysCode;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.UserDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.entity.AddressEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CompanyEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.GeoEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;
import com.bootcamp.sb.demo_sb_bc_forum.repository.UserRepository;
import com.bootcamp.sb.demo_sb_bc_forum.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public List<UserDto> getUsersFromUrl() {
        String url = "https://jsonplaceholder.typicode.com/users";
        
        List<UserDto> userDtos = 
        Arrays.asList(this.restTemplate.getForObject(url, UserDto[].class));

        List<UserDto> filteredUserDtos = userDtos.stream().map(e -> UserDtoMapper.map(e)).collect(Collectors.toList());
    
        return filteredUserDtos;
    }

    @Override
    public ApiResp<List<UserEntity>> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return ApiResp.<List<UserEntity>>builder().syscode(SysCode.SUCCESS).data(users).build();
    }

    @Override
    public ApiResp<UserEntity> getUserById(String id) {
        Long userId;

        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        UserEntity user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return globalExceptionHandler.handleUserNotFoundException();
        }
        return ApiResp.<UserEntity>builder().syscode(SysCode.SUCCESS).data(user).build();
    }

    @Override
    public ApiResp<UserEntity> replaceUser(Long id, UserDto userDto) {
        UserEntity userToBeReplaced = this.userRepository.findById(id).orElse(null);
    
        if (userToBeReplaced != null) {
            // Update basic fields
            userToBeReplaced.setName(userDto.getName());
            userToBeReplaced.setUsername(userDto.getUsername());
            userToBeReplaced.setEmail(userDto.getEmail());
            userToBeReplaced.setPhone(userDto.getPhone());
            userToBeReplaced.setWebsite(userDto.getWebsite());
    
            // Update or create Address
            UserDto.Address addressDto = userDto.getAddress();
            AddressEntity address;
            if (addressDto != null) {
                if (userToBeReplaced.getAddress() != null) {
                    // If the user already has an address, update it
                    address = userToBeReplaced.getAddress();
                } else {
                    // If not, create a new AddressEntity
                    address = new AddressEntity();
                    userToBeReplaced.setAddress(address); // Set the address in the user
                }
    
                // Update address fields
                address.setStreet(addressDto.getStreet());
                address.setSuite(addressDto.getSuite());
                address.setCity(addressDto.getCity());
                address.setZipcode(addressDto.getZipcode());
                address.setUser(userToBeReplaced); // Set the back-reference
    
                // Update Geo
                UserDto.Address.Geo geoDto = addressDto.getGeo();
                GeoEntity geo;
                if (geoDto != null) {
                    if (address.getGeo() != null) {
                        // If the address already has a geo, update it
                        geo = address.getGeo();
                    } else {
                        // If not, create a new GeoEntity
                        geo = new GeoEntity();
                        address.setGeo(geo); // Associate geo with address
                    }
                    // Update geo fields
                    geo.setLat(geoDto.getLat());
                    geo.setLng(geoDto.getLng());
                    geo.setAddress(address); // Set the back-reference
                }
            }
    
            // Update or create Company
            UserDto.Company companyDto = userDto.getCompany();
            if (companyDto != null) {
                CompanyEntity company;
                if (userToBeReplaced.getCompany() != null) {
                    // If the user already has a company, update it
                    company = userToBeReplaced.getCompany();
                } else {
                    // If not, create a new CompanyEntity
                    company = new CompanyEntity();
                    userToBeReplaced.setCompany(company); // Set the company in the user
                }
                // Update company fields
                company.setName(companyDto.getName());
                company.setCatchPhrase(companyDto.getCatchPhrase());
                company.setBs(companyDto.getBs());
                company.setUser(userToBeReplaced); // Set the back-reference here
            }
    
            // Save the updated user entity
            UserEntity newUser = this.userRepository.save(userToBeReplaced);
            return globalExceptionHandler.handleSuccessfulResponse(newUser);
        }
    
        // Handle the case where the user is not found
        return globalExceptionHandler.handleUserNotFoundException();
    }

}
