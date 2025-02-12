package com.bootcamp.sb.demo_sb_customer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.bootcamp.sb.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;

public interface CustomerOperation {
    @PostMapping(value = "/customer")
    ApiResp<CustomerEntity> createCustomer(@RequestBody CustomerEntity customerEntity);
  
    @GetMapping(value = "/customers")
    ApiResp<List<CustomerEntity>> getCustomers();

}

