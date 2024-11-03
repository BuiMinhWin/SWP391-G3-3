package com.example.demo.repository;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Integer> {
}
