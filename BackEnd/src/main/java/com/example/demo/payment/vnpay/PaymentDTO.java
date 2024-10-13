package com.example.demo.payment.vnpay;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PaymentDTO {
    @Builder
    public static class VNPayResponse {
        private String code;
        private String message;
        private String paymentUrl;
    }
}
