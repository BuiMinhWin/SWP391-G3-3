package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private String orderId;
    private String accountId;
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private String origin;
    private String destination;
    private String freight;
    private float totalPrice;
    private String serviceId;
    private String documentId;
    private int status;
}
