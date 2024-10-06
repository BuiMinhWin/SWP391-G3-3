package com.example.demo.service.iml;


import com.example.demo.dto.request.AccountDTO;
import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.IdGenerator;
import com.example.demo.entity.Order;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        order.setDocumentId(IdGenerator.generateId());
        order.setServiceId(IdGenerator.generateId());

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

    public OrderDTO cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() != 0) {
            order.setStatus(0);
            order.setOrderDate(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            return orderMapper.mapToOrderDTO(savedOrder);
        } else {
            throw new IllegalStateException("Order is already canceled.");
        }
    }

    public OrderDTO updateOrderWhenCanceled(String orderId, OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() == 0) {

            if (orderDTO.getOrderDate() != null) {
                order.setOrderDate(orderDTO.getOrderDate());
            }

            if (orderDTO.getShippedDate() != null) {
                order.setShippedDate(orderDTO.getShippedDate());
            }

            if (orderDTO.getOrigin() != null) {
                order.setOrigin(orderDTO.getOrigin());
            }

            if (orderDTO.getDestination() != null) {
                order.setDestination(orderDTO.getDestination());
            }

            if (orderDTO.getFreight() != null) {
                order.setFreight(orderDTO.getFreight());
            }

            if (orderDTO.getTotalPrice() != 0) {
                order.setTotalPrice(orderDTO.getTotalPrice());
            }

            if (orderDTO.getAccountId() != null) {
                Account account = accountRepository.findById(orderDTO.getAccountId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderDTO.getAccountId()));
                order.setAccount(account);
            }
            order.setStatus(1);
            Order updatedOrder = orderRepository.save(order);

            return orderMapper.mapToOrderDTO(updatedOrder);
        } else {
            throw new IllegalStateException("Order can only be updated if it is canceled.");
        }
    }
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::mapToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        return orderMapper.mapToOrderDTO(order);
    }


}