package com.bootcamp.sb.demo_sb_customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.sb.demo_sb_customer.codewave.BusinessException;
import com.bootcamp.sb.demo_sb_customer.codewave.SysCode;
import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.sb.demo_sb_customer.entity.OrderEntity;
import com.bootcamp.sb.demo_sb_customer.service.OrderService;
import com.bootcamp.sb.demo_sb_customer.repository.CustomerRepository;
import com.bootcamp.sb.demo_sb_customer.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;

    // if customer id not found, throw new BusinessException("Customer ID not found")
    @Override
    public OrderEntity createOrder(Long customerId, OrderEntity orderEntity) {
        CustomerEntity customerEntity = this.customerRepository.findById(customerId)
        .orElseThrow(() -> BusinessException.of(SysCode.ID_NOT_FOUND));

        orderEntity.setCustomerEntity(customerEntity);
        // Save orderEntity to DB
        return this.orderRepository.save(orderEntity);
    }

    @Override
    public List<OrderEntity> getOrders() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrders'");
    }
}
