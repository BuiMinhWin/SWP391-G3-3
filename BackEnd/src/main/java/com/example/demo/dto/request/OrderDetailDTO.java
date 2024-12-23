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
    private String koiType;
    private LocalDateTime createdAt;
    private String koiName;
    private int status;
}
