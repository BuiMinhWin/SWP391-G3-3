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
                document.getImageData(),
                document.getFileType(),
                document.getFileName()
        );
    }

    public Document mapToDocument(DocumentDTO documentDTO, Order order) {
        Document document = new Document();
        document.setDocumentId(documentDTO.getDocumentId());
        document.setOrder(order);
        document.setImageData(documentDTO.getImageData());
        document.setFileType(documentDTO.getFileType());
        document.setFileName(documentDTO.getFileName());
        return document;
    }
}
