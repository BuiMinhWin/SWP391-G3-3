package com.example.demo.controller;

import com.example.demo.service.iml.DocumentService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping(value = "/{orderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "File to upload", required = true)
            @RequestPart("document_file") MultipartFile file,
            @PathVariable("orderId") String orderId) throws IOException {

        String uploadImage = documentService.uploadImage(file, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }



    @GetMapping("/download/order/{orderId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable String orderId) {
        byte[] imageData = documentService.downloadImage(orderId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(imageData));
    }


}
