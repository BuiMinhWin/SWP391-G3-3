package com.example.demo.repository;

import com.example.demo.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services, String> {
}
