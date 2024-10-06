package com.example.demo.mapper;

import com.example.demo.dto.request.DocumentDTO;
import com.example.demo.entity.Document;
import com.example.demo.entity.Order;

public class DocumentMapper {

    public static DocumentDTO maptoDocumentDTO(Document document) {
        return new DocumentDTO(
                document.getOrder().getDocumentId(),
                document.getDocumentType(),
                document.getDescription()
        );
    }

    public static Document mapToDocument(DocumentDTO documentDTO, Order order) {
        Document document = new Document();
        document.setOrder(order);
        document.setDocumentType(documentDTO.getDocument_type());
        document.setDescription(documentDTO.getDescription());
        return document;
    }
}
