package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
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

    @PostMapping(value = "/{orderDetailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "File to upload", required = true)
            @RequestPart("document_file") MultipartFile file,
            @PathVariable("orderDetailId") String orderDetailId) throws IOException {

        String uploadImage = documentService.uploadImage(file, orderDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }



    @GetMapping("/download/order/{orderDetailId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable String orderDetailId) {
        byte[] imageData = documentService.downloadImage(orderDetailId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(imageData));
    }

    @PutMapping(value = "/update/{orderDetailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateImage(@RequestParam("document_file") MultipartFile file,
                                              @PathVariable String orderDetailId) {
        try {
            String fileName = documentService.updateImage(file, orderDetailId);
            return new ResponseEntity<>("Image updated successfully: " + fileName, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
