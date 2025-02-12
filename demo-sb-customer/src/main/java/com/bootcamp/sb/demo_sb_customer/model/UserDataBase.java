package com.bootcamp.sb.demo_sb_customer.model;

import java.util.ArrayList;
import java.util.List;

import com.bootcamp.sb.demo_sb_customer.model.dto.UserDto;


public class UserDataBase {
    public static List<UserDto> users = new ArrayList<>();

    public static boolean put(UserDto userDto) {
        if (!(users.contains(userDto))) {
            users.add(userDto);
            return true;
        }
        return false;
    }
}
