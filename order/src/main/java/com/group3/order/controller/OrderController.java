package com.group3.order.controller;

import com.group3.order.dto.request.OrderCreation;
import com.group3.order.dto.request.OrderUpdate;
import com.group3.order.entity.Order;
import com.group3.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    Order createOrder(@RequestBody OrderCreation request){
       return orderService.createOrder(request);
    }
    @GetMapping
    List<Order> getOrder(){
        return orderService.getOrder();
    }
    @DeleteMapping("/{orderId}")
    String deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
        return "Order has been deleted";
    }
    @PutMapping("/{orderId}")
    Order updateAccount(@PathVariable String orderId, @RequestBody OrderUpdate request){
        return orderService.updateOrder(orderId, request);

    }
    @GetMapping("/{orderId}")
    Order getOrder(@PathVariable("orderId") String orderId){
        return orderService.getOrders(orderId);
    }


}
