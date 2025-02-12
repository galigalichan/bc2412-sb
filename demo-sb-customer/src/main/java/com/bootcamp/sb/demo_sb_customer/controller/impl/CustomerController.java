package com.bootcamp.sb.demo_sb_customer.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_customer.codewave.SysCode;
import com.bootcamp.sb.demo_sb_customer.controller.CustomerOperation;
import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.sb.demo_sb_customer.service.CustomerService;

@RestController
public class CustomerController implements CustomerOperation {
    @Autowired
    private CustomerService customerService;

    // Construction Injection
    // @Autowired
    // public CustomerController(CustomerService customerService) {
    //     this.customerService = customerService;
    // }

  @Override
  public ApiResp<List<CustomerEntity>> getCustomers() {
    List<CustomerEntity> customerEntities = this.customerService.getCustomers();
    return ApiResp.<List<CustomerEntity>>builder()
      .syscode(SysCode.OK)
      .data(customerEntities)
      .build();
  }
  
  @Override
  public ApiResp<CustomerEntity> createCustomer(CustomerEntity customerEntity) {
    CustomerEntity serviceResult = this.customerService.createCustomer(customerEntity);
    return ApiResp.<CustomerEntity>builder()
      .syscode(SysCode.OK)
      .data(serviceResult)
      .build();
  }

}
