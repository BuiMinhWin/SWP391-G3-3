package com.example.demo.repository;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByOrder(Order order);
    List<Feedback> findByOrder_OrderId(String orderId);
}
