package com.example.demo.payment.vnpay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PaymentDTO {
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VNPayResponse {
        private String code;
        private String message;
        private String paymentUrl;
    }
}
