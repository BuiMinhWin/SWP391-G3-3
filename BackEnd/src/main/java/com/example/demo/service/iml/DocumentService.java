package com.example.demo.service.iml;

import com.example.demo.dto.request.DocumentDTO;
import com.example.demo.entity.Document;
import com.example.demo.entity.Order;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DocumentMapper;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final OrderRepository orderRepository;
    private final DocumentMapper documentMapper;


    public DocumentDTO createDocument(DocumentDTO documentDTO) {
        if (documentDTO == null) {
            throw new IllegalArgumentException("DocumentDTO must not be null");
        }

        Order order = orderRepository.findById(documentDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + documentDTO.getOrderId()));

        Document document = documentMapper.mapToDocument(documentDTO, order);
        Document savedDocument = documentRepository.save(document);
        return documentMapper.mapToDocumentDTO(savedDocument);
    }

    public List<DocumentDTO> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        return documents.stream()
                .map(documentMapper::mapToDocumentDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByOrderId(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<Document> documents = documentRepository.findByOrder(order);
        return documents.stream()
                .map(documentMapper::mapToDocumentDTO)
                .collect(Collectors.toList());
    }

    public DocumentDTO updateDocument(String documentId, DocumentDTO documentDTO) {
        Document existingDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        if (documentDTO.getOrderId() != null) {
            Order order = orderRepository.findById(documentDTO.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + documentDTO.getOrderId()));
            existingDocument.setOrder(order);
        }

        existingDocument.setDocumentType(documentDTO.getDocumentType());
        existingDocument.setDescription(documentDTO.getDescription());

        Document updatedDocument = documentRepository.save(existingDocument);
        return documentMapper.mapToDocumentDTO(updatedDocument);
    }

    public void deleteDocument(String documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
        documentRepository.delete(document);
    }
}
