package com.demo.helloworld.demo_sb_helloworld;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Serialization (Object -> JSON)
@AllArgsConstructor
public class Cat {
    private String name;
    private int age;


}
