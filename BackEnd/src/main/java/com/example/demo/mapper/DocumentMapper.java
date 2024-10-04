package com.example.demo.mapper;

import com.example.demo.dto.request.DocumentDTO;
import com.example.demo.entity.Document;

public class DocumentMapper {

    public static DocumentDTO maptoDocumentDTO(Document document) {
        return new DocumentDTO(
                document.getDocumentId(),
                document.getDocument_type(),
                document.getDescription()
        );
    }

    public static Document mapToDocument(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setDocumentId(documentDTO.getDocumentId());
        document.setDocument_type(documentDTO.getDocument_type());
        document.setDescription(documentDTO.getDescription());
        return document;
    }
}
