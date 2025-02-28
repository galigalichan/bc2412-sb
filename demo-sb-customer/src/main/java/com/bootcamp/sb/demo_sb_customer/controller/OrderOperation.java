package com.bootcamp.sb.demo_sb_customer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.bootcamp.sb.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_customer.entity.OrderEntity;

public interface OrderOperation {
    // http://localhost:8100/order?cid=2
    @PostMapping(value = "/order")
    ApiResp<OrderEntity> createOrder(@RequestParam(value = "cid") Long customerId, @RequestBody OrderEntity orderEntity);

    // @GetMapping(value = "/orders")
    // ApiResp<List<OrderEntity>> getOrders();
    
}
