package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DocumentDTO {
    private String documentId;
    private String orderDetailId;
    private byte[] imageData;
    private String fileType;
    private String fileName;
}
