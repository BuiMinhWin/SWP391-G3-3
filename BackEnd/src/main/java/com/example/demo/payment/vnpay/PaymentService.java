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
            OrderDTO orderDTO = orderService.getOrderByIdV2(orderId); // Đảm bảo phương thức này đã được sửa đúng cách
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
        } catch (NumberFormatException e) {
            log.error("Invalid amount format for orderId {}: {}", orderId, e.getMessage());
            return PaymentDTO.VNPayResponse.builder()
                    .code("400")
                    .message("Invalid amount format")
                    .build();
        } catch (OrderNotFoundException e) {
            log.error("Order not found for orderId {}: {}", orderId, e.getMessage());
            return PaymentDTO.VNPayResponse.builder()
                    .code("404")
                    .message(e.getMessage())
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
}
