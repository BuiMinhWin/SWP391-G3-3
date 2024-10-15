package com.example.demo.repository;

import com.example.demo.entity.DeliveryStatus;
import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, String> {
    List<DeliveryStatus> findByOrder(Order order);

}
