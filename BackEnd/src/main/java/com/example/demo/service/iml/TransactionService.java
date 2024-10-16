package com.example.demo.service.iml;

import com.example.demo.entity.Order;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final OrderRepository orderRepository;

    public Transaction createTransaction(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Transaction transaction = new Transaction();
            transaction.setOrder(order);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setStatus(order.getStatus()); // Set initial status

            System.out.println("Creating transaction with Order status: " + order.getStatus());

            return transactionRepository.save(transaction);
        } else {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
    }

    public Transaction updateTransactionStatus(String transactionId, int newStatus) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

        if (transactionOptional.isPresent()) {
            Transaction originalTransaction = transactionOptional.get();

            // Create a new transaction for the status update
            Transaction newTransaction = new Transaction();
            newTransaction.setOrder(originalTransaction.getOrder());
            newTransaction.setTransactionDate(LocalDateTime.now());
            newTransaction.setStatus(newStatus); // Set the new status

            // Save the new transaction entry with the updated status
            transactionRepository.save(newTransaction);

            // Optionally: log the status update or any additional actions here
            System.out.println("Updated transaction with ID: " + transactionId +
                    " to new status: " + newStatus);

            return newTransaction; // Return the new transaction entry
        } else {
            throw new IllegalArgumentException("Transaction not found with ID: " + transactionId);
        }
    }
}
