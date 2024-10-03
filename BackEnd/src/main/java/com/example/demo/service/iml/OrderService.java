package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Account;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, AccountRepository accountRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Find the account associated with the order
        Account account = accountRepository.findById(orderDTO.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with ID '" + orderDTO.getAccountId() + "' not found."));

        // Map the DTO to an Order entity
        Order order = orderMapper.mapToOrder(orderDTO, account);

        // Set default values for order fields if needed
        setDefaultOrderValues(order, orderDTO);

        // Save the order to the repository
        Order savedOrder = orderRepository.save(order);

        // Convert saved entity back to DTO and return
        return orderMapper.mapToOrderDTO(savedOrder);
    }

    // Helper method to set default values
    private void setDefaultOrderValues(Order order, OrderDTO orderDTO) {
        if (orderDTO.getOrderId() == null) {
            order.setOrderId(UUID.randomUUID().toString()); // Generate UUID
        }
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        if (order.getStatus() == 0) {
            order.setStatus(1); // Default status
        }
    }
}
