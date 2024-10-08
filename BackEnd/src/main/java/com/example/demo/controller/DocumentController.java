package com.example.demo.controller;

import com.example.demo.dto.request.DocumentDTO;
import com.example.demo.entity.Document;
import com.example.demo.service.iml.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;


    @PostMapping("/create")
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentDTO documentDTO) {
        DocumentDTO createdDocument = documentService.createDocument(documentDTO);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documents = documentService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByOrderId(@PathVariable String orderId) {
        List<DocumentDTO> documents = documentService.getDocumentsByOrderId(orderId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable String documentId, @RequestBody DocumentDTO documentDTO) {
        DocumentDTO updatedDocument = documentService.updateDocument(documentId, documentDTO);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        documentService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
