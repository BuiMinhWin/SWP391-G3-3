package com.example.demo.repository;

import com.example.demo.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, String> {
    Optional<Document> findByOrderDetailOrderDetailId(String orderDetailId);
}
