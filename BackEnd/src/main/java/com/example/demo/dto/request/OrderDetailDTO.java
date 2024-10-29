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
public class OrderDetailDTO {

    private String orderDetailId;
    private String orderId;
    private int quantity;
    private float weight;
    private String discount;
    private String koiType;
    private LocalDateTime createdAt;
    private String koiName;
    private int status;
    private int totalServicePrice;
    private String serviceId1;
    private String serviceId2;
    private String serviceId3;
    private int servicePrice1;
    private int servicePrice2;
    private int servicePrice3;
}
