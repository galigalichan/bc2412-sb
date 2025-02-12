package com.bootcamp.sb.demo_sb_restful.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Cat {
    // Wrapper class for serialization / deserialization
    private Long id; // Wrapper Class Long supports null, primitive long does not
    private String name;
    private Integer age;
}
