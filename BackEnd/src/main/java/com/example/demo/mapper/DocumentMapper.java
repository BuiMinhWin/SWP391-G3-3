package com.example.demo.mapper;

import com.example.demo.dto.request.DocumentDTO;
import com.example.demo.entity.Document;
import com.example.demo.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentDTO mapToDocumentDTO(Document document) {
        return new DocumentDTO(
                document.getDocumentId(),
                document.getOrder().getOrderId(),
                document.getDocumentFile()
        );
    }

    public Document mapToDocument(DocumentDTO documentDTO, Order order) {
        Document document = new Document();
        document.setDocumentId(documentDTO.getDocumentId());
        document.setOrder(order);
        document.setDocumentFile(documentDTO.getDocumentFile());
        return document;
    }
}
