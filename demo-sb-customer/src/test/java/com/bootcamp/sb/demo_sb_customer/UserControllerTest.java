package com.bootcamp.sb.demo_sb_customer;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bootcamp.sb.demo_sb_customer.controller.impl.UserController;
import com.bootcamp.sb.demo_sb_customer.dto.UserDTO;
import com.bootcamp.sb.demo_sb_customer.dto.mapper.UserDTOMapper;
import com.bootcamp.sb.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.sb.demo_sb_customer.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// @SpringBootTest -> Real full package test
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
        // @Test
        // void contextLoad() {
        // }
        @MockBean
        private UserService userService; // 10 method, mock 10 method
        @SpyBean
        private UserDTOMapper userDTOMapper; // 10 method, real 10 method, still mock method
    
        @Autowired
        private MockMvc mockMvc;
    
        // @Test
        void testGetUsers() throws Exception { // call by test instance
            // Controller: convert List of Dto to List of DTO
            // John
            UserDto.Address.Geo johnGeo = UserDto.Address.Geo.builder().build();
            UserDto.Address johnAddress = 
                UserDto.Address.builder().geo(johnGeo).build();
            UserDto.Company johnCompany = UserDto.Company.builder().build();
            UserDto john = UserDto.builder()
                .name("John")
                .address(johnAddress)
                .company(johnCompany).build();

            // Mary
            UserDto.Address.Geo maryGeo = UserDto.Address.Geo.builder().build();
            UserDto.Address maryAddress =
                UserDto.Address.builder().geo(maryGeo).build();
            UserDto.Company maryCompany = UserDto.Company.builder().build();
            UserDto mary = UserDto.builder().name("Mary").address(maryAddress)
                .company(maryCompany).build();

            List<UserDto> userDtos = Arrays.asList(john, mary);

            Mockito.when(userService.getUsers()).thenReturn(userDtos);

            ResultActions result = mockMvc.perform(get("/jsonplaceholder/users"));
            result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            // To check the data:
            String json = result.andReturn().getResponse().getContentAsString();
            List<UserDTO> userDTOs = new ObjectMapper().readValue(json,
                new TypeReference<List<UserDTO>>() {});

            MatcherAssert.assertThat(userDTOs, Matchers.containsInAnyOrder( //
                Matchers.hasProperty("name", Matchers.equalTo("John")), //
                Matchers.hasProperty("name", Matchers.equalTo("Mary")) //
            ));

        
    }
    
}
