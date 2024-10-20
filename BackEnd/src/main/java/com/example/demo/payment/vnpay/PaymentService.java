package com.example.demo.payment.vnpay;

import com.example.demo.config.VNPAYConfig;
import com.example.demo.dto.request.OrderDTO;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.service.iml.OrderService;
import com.example.demo.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final VNPAYConfig vnPayConfig;
    private final OrderService orderService;

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request, String orderId, String bankCode) {
        try {
            log.debug("Creating VNPay payment for orderId: {}", orderId);

            if (orderId == null || orderId.trim().isEmpty()) {
                log.error("Invalid orderId: {}", orderId);
                throw new IllegalArgumentException("orderId cannot be null or empty");
            }

            // Lấy thông tin đơn hàng
            OrderDTO orderDTO = orderService.getOrderByIdV2(orderId);
            log.info("Total price for orderId {}: {} VND", orderId, orderDTO.getTotalPrice());

            // Tính toán số tiền
            BigDecimal amount = BigDecimal.valueOf(orderDTO.getTotalPrice()).multiply(new BigDecimal(100));

            // Lấy cấu hình VNPay
            Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
            vnpParamsMap.put("vnp_Amount", amount.toPlainString());

            if (bankCode != null && !bankCode.isEmpty()) {
                vnpParamsMap.put("vnp_BankCode", bankCode);
            }

            vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

            // Use the fixed return URL
            vnpParamsMap.put("vnp_ReturnUrl", "http://localhost:3000/payment-outcome");

            // Tạo vnp_TxnRef mới bằng UUID
            String vnpTxnRef = UUID.randomUUID().toString();
            vnpParamsMap.put("vnp_TxnRef", vnpTxnRef);

            // Set vnp_OrderInfo với thông tin đơn hàng
            vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang: " + orderId);

            // Tạo URL thanh toán và secure hash
            String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
            String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
            String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
            log.info("Payment URL generated: {}", paymentUrl);

            // Lưu vnpTxnRef vào đơn hàng
            orderService.updateVnpTxnRef(orderId, vnpTxnRef);

            return PaymentDTO.VNPayResponse.builder()
                    .code("200")
                    .message("Payment URL created successfully")
                    .paymentUrl(paymentUrl)
                    .build();
        } catch (Exception e) {
            log.error("Error while creating VNPay payment for orderId {}: {}", orderId, e.getMessage());
            return PaymentDTO.VNPayResponse.builder()
                    .code("500")
                    .message("Internal Server Error")
                    .build();
        }
    }

    public String extractAndLogTxnRef(HttpServletRequest request) {
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        log.info("Extracted vnp_TxnRef: {}", vnpTxnRef);
        return vnpTxnRef;
    }

    public PaymentDTO.VNPayResponse handlePayCallback(String vnpTxnRef, String status) {
        try {
            OrderDTO orderDTO = orderService.getOrderByVnpTxnRef(vnpTxnRef);

            if ("00".equals(status)) {
                orderService.updatePaymentStatus(orderDTO.getOrderId(), true);
                orderService.updateOrderStatus(orderDTO.getOrderId(), 1);
                return new PaymentDTO.VNPayResponse("00", "Success", "http://localhost:3000/payment-outcome");
            } else {
                orderService.updateOrderStatus(orderDTO.getOrderId(), 0);
                return new PaymentDTO.VNPayResponse("01", "Failed", null);
            }
        } catch (OrderNotFoundException e) {
            log.warn("No order found for vnpTxnRef: {}", vnpTxnRef);
            return new PaymentDTO.VNPayResponse("404", "Order not found", null);
        } catch (Exception e) {
            log.error("Error processing VNPay callback for vnpTxnRef: {}: {}", vnpTxnRef, e.getMessage());
            return new PaymentDTO.VNPayResponse("500", "Internal Server Error", null);
        }
    }
}
