package com.example.demo.controller;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.dto.request.UpdateStatusDTO;
import com.example.demo.service.iml.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateStatusDTO updateStatusDTO) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, updateStatusDTO.getNewStatus());
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
