package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByVnpTxnRef(String vnpTxnRef);
    List<Order> findByAccount_AccountId(String accountId);
    List<Order> findByDeliver(String deliver);
}
