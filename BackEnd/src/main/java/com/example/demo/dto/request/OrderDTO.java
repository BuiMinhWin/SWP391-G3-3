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

    private String receiverName;
    private String senderName;
    private String receiverAddress;
    private String senderAddress;
    private String receiverPhone;
    private String senderPhone;
    private int postalCode;
    private String receiverNote;
    private String senderNote;
    private String orderNote;

    private float totalPrice;
    private int status;
}
