package com.example.demo.service.iml;

import com.example.demo.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

}
