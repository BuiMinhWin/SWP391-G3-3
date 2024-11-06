package com.example.demo.controller;

import com.example.demo.dto.request.OrderDetailDTO;
import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.service.iml.OrderDetailService;
import com.example.demo.service.iml.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordersDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ServicesService servicesService;

    @PostMapping("/create")
    public ResponseEntity<List<OrderDetailDTO>> createMultipleOrderDetails(@RequestBody List<OrderDetailDTO> orderDetailDTOs) {
        List<OrderDetailDTO> createdOrderDetails = orderDetailService.createOrderDetail(orderDetailDTOs);
        return new ResponseEntity<>(createdOrderDetails, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable String orderId) {
        List<OrderDetailDTO> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
}
