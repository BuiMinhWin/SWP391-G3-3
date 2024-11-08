package com.example.demo.service.iml;

import com.example.demo.entity.Order;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
