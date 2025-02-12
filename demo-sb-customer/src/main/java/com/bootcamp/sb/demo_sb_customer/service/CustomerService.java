package com.bootcamp.sb.demo_sb_customer.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;

public interface CustomerService {
    List<CustomerEntity> getCustomers();
    CustomerEntity createCustomer(CustomerEntity customerEntity);
}
