package com.group3.order.service;

import com.group3.order.dto.request.OrderCreation;
import com.group3.order.entity.Order;
import com.group3.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(OrderCreation request){
        Order order = new Order();

        order.setAccountId(request.getAccountId());
        order.setOrderDate(request.getOrderDate());
        order.setShippedDate(request.getShippedDate());
        order.setOrigin(request.getOrigin());
        order.setDestination(request.getDestination());
        order.setFreight(request.getFreight());
        order.setTotalPrice(request.getTotalPrice());
        order.setServiceId(request.getServiceId());
        order.setDocumentId(request.getDocumentId());
        order.setStatus(request.getStatus());

        return orderRepository.save(order);
    }

    public List<Order> getOrder(){
        return orderRepository.findAll();
    }
    public Order getOrders(String orderId){
        return orderRepository.findById(orderId).orElseThrow(()
                -> new RuntimeException("Order not found"));
    }
    public void deleteOrder(String orderId){
        orderRepository.deleteById(orderId);
    }

}
