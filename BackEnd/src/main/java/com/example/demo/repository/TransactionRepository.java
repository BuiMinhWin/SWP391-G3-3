package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByOrder(Order order);
}
