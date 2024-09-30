package com.group3.order.controller;

import com.group3.order.dto.request.OrderCreation;
import com.group3.order.entity.Order;
import com.group3.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    Order createOrder(@RequestBody OrderCreation request){
       return orderService.createOrder(request);
    }

}
