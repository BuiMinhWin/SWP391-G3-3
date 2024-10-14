package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Integer> {
    List<Services> findByOrder(Order order);
    Optional<Services> findByOrderAndServicesId(Order order, Integer servicesId);
}
