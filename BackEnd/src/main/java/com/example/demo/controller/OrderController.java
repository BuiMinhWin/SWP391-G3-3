package com.example.demo.controller;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.dto.request.OrderDTO;
import com.example.demo.service.iml.AccountService;
import com.example.demo.service.iml.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")

public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO savedOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable String orderId) {
        OrderDTO canceledOrder = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(canceledOrder, HttpStatus.OK);
    }
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderWhenCanceled(@PathVariable String orderId, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrderWhenCanceled(orderId, orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
