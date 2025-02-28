package com.bootcamp.sb.demo_sb_customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.sb.demo_sb_customer.repository.CustomerRepository;
import com.bootcamp.sb.demo_sb_customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<CustomerEntity> getCustomers() {
        return this.customerRepository.findAll(); // john 20, mary 40
        // we receive John and Mary from Repository and return John and Mary to client
        // Test case: make sure there is no extra addition or subtraction performed in this method findAll()
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        return this.customerRepository.save(customerEntity);
    }

    @Override
    public List<CustomerEntity> getCustomersByJPQL(String customerName) {
      return this.customerRepository.findByNameByJPQL(customerName);
    }
  
    @Override
    public List<CustomerEntity> getCustomersByNQ(String customerName) {
      return this.customerRepository.findByNameByNativeQuery(customerName);
    }

}
