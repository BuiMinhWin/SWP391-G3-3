package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.dto.request.TransactionDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final OrderRepository orderRepository;
    private final OrderService orderService;


    public Transaction createTransaction(String orderId, int totalPrice) {
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setTotalPrice(totalPrice);
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }


    public List<Transaction> getTransactionsByOrderId(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return transactionRepository.findByOrder(order);
        } else {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
    }
}
