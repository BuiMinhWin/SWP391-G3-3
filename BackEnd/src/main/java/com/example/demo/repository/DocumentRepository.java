package com.example.demo.repository;

import com.example.demo.entity.Document;
import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findByOrder(Order order);
}
