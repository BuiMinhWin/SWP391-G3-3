package com.example.demo.service.iml;

import com.example.demo.entity.Document;
import com.example.demo.entity.Order;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final OrderRepository orderRepository;

    public String uploadImage(MultipartFile file, String orderId) throws IOException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Document imageData = documentRepository.save(
                Document.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .order(order)
                        .build()
        );

        if (imageData != null){
            return file.getName();
        }
        return null;
    }

    public byte[] downloadImage(String orderId) {
        // Find the document associated with the given orderId
        Document document = documentRepository.findByOrderOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found for order with id: " + orderId));

        // Decompress the image data before returning
        return ImageUtils.decompressImage(document.getImageData());
    }

}
