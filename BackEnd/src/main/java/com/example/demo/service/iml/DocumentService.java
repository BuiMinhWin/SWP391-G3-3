package com.example.demo.service.iml;

import com.example.demo.entity.Document;
import com.example.demo.entity.OrderDetail;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final OrderDetailRepository orderDetailRepository;

    public String uploadImage(MultipartFile file, String orderDetailId) throws IOException {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Order detail not found with id: " + orderDetailId));

        Document imageData = documentRepository.save(
                Document.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .orderDetail(orderDetail)
                        .build()
        );

        if (imageData != null){
            return file.getName();
        }
        return null;
    }

    public byte[] downloadImage(String orderDetailId) {

        Document document = documentRepository.findByOrderDetailOrderDetailId(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found for order with id: " + orderDetailId));

        return ImageUtils.decompressImage(document.getImageData());
    }

}
