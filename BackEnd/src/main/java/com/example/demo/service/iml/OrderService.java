package com.example.demo.service.iml;


import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final OrderMapper orderMapper;

    public OrderDTO createOrder(OrderDTO orderDTO) {

        Account account = accountRepository.findById(orderDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderDTO.getAccountId()));

        Order order = orderMapper.mapToOrder(orderDTO, account);

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
