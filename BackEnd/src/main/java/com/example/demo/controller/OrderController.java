package com.example.demo.controller;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.dto.request.UpdateStatusDTO;
import com.example.demo.service.iml.OrderService;
import com.example.demo.service.iml.ServicesService;
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

    @Autowired
    private ServicesService servicesService;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO savedOrder = orderService.createOrder(orderDTO);

        ServicesDTO servicesDTO = new ServicesDTO();
        servicesDTO.setOrderId(savedOrder.getOrderId());
        servicesService.createServicesForOrder(servicesDTO);

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable String orderId) {
        OrderDTO canceledOrder = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(canceledOrder, HttpStatus.OK);
    }

    @PatchMapping("/update/{orderId}")
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

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByAccountId(@PathVariable String accountId) {
        List<OrderDTO> orders = orderService.getOrdersByAccountId(accountId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PatchMapping("/updateStatus/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateStatusDTO updateStatusDTO) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, updateStatusDTO.getNewStatus());
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
