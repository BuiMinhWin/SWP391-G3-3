package com.example.demo.service.iml;


import com.example.demo.dto.request.AccountDTO;
import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.IdGenerator;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor

public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final OrderMapper orderMapper;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        System.out.println("Received OrderDTO: " + orderDTO);

        Account account = accountRepository.findById(orderDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderDTO.getAccountId()));

        System.out.println("Account found: " + account);

        Order order = orderMapper.mapToOrder(orderDTO, account);
        order.setOrderId(IdGenerator.generateId());

        if (orderDTO.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (orderDTO.getStatus() == 0) {
            order.setStatus(1);
        }

        System.out.println("Creating Order with Account ID: " + order.getAccount().getAccountId());

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapToOrderDTO(savedOrder);
    }



}