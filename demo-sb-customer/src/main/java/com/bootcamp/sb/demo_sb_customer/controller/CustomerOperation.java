package com.bootcamp.sb.demo_sb_customer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bootcamp.sb.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;

public interface CustomerOperation {
    @GetMapping(value = "/customers")
    @ResponseStatus(HttpStatus.OK) // 200 (for all others like get, delete)
    ApiResp<List<CustomerEntity>> getCustomers();

    @PostMapping(value = "/customer")
    @ResponseStatus(HttpStatus.CREATED) // 201 (for create)
    ApiResp<CustomerEntity> createCustomer(@RequestBody CustomerEntity customerEntity);

    @GetMapping(value = "/jpql/customers")
    @ResponseStatus(HttpStatus.OK)
    List<CustomerEntity> getCustomerByJPQL(@RequestParam String name);

    @GetMapping(value = "/nq/customers")
    @ResponseStatus(HttpStatus.OK)
    List<CustomerEntity> getCustomerByNQ(@RequestParam String name);


}

