package com.example.demo.payment.vnpay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String orderId;
    private String bankCode;
}
