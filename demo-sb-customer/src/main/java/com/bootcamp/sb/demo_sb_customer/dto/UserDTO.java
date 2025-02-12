package com.bootcamp.sb.demo_sb_customer.dto;

// import com.bootcamp.sb.demo_sb_customer.model.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Features of DTO
// ! 1. Different o
// ! Different numbers of fields for the API
// This DTO is for Serialization
@Getter
@Builder
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Address address;

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        @Getter
        @Builder
        @Setter
        @AllArgsConstructor
        public static class Geo {
            @JsonProperty(value = "x")
            private Double latitude;
            @JsonProperty(value = "y")
            private Double longitude;
        }
    }

    
}
