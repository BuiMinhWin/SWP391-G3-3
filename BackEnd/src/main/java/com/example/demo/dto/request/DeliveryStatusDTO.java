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
public class DeliveryStatusDTO {

    private String deliveryStatusId;
    private String orderId;
    private LocalDateTime timeTracking;
    private String currentLocate;
    private int status;
}
