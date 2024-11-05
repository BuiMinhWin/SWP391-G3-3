package com.example.demo.mapper;

import com.example.demo.dto.request.DocumentDTO;
import com.example.demo.entity.Document;
import com.example.demo.entity.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentDTO mapToDocumentDTO(Document document) {
        return new DocumentDTO(
                document.getDocumentId(),
                document.getOrderDetail().getOrderDetailId(),
                document.getImageData(),
                document.getFileType(),
                document.getFileName()
        );
    }

    public Document mapToDocument(DocumentDTO documentDTO, OrderDetail orderDetail) {
        Document document = new Document();
        document.setDocumentId(documentDTO.getDocumentId());
        document.setOrderDetail(orderDetail);
        document.setImageData(documentDTO.getImageData());
        document.setFileType(documentDTO.getFileType());
        document.setFileName(documentDTO.getFileName());
        return document;
    }
}
