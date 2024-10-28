package com.example.demo.payment.vnpay;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.iml.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(
            @Valid @RequestBody PaymentRequest paymentRequest,
            BindingResult bindingResult,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.error("Validation errors: {}", errorMessage);
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, errorMessage, null);
        }

        log.debug("Received payment request for orderId: {}", paymentRequest.getOrderId());

        PaymentDTO.VNPayResponse response = paymentService.createVnPayPayment(
                request, paymentRequest.getOrderId(), paymentRequest.getBankCode());

        return new ResponseObject<>(HttpStatus.OK, "Success", response);
    }

    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) {
        String status = request.getParameter("vnp_ResponseCode");
        String vnpTxnRef = paymentService.extractAndLogTxnRef(request);

        log.debug("Received VNPay callback for vnpTxnRef: {} with status: {}", vnpTxnRef, status);
        PaymentDTO.VNPayResponse paymentResponse = paymentService.handlePayCallback(vnpTxnRef, status, response);

        if ("00".equals(paymentResponse.getCode())) {
            try {
                response.sendRedirect("http://localhost:3000/payment-outcome");
            } catch (IOException e) {
                log.error("Error during redirection: {}", e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}
