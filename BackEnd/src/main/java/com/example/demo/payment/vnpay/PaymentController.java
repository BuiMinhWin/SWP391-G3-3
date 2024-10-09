package com.example.demo.payment.vnpay;

import com.example.demo.response.ResponseObject;
import com.example.demo.service.iml.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    // Endpoint POST để tạo thanh toán VNPay dựa trên orderId
    @PostMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(
            @Valid @RequestBody PaymentRequest paymentRequest,
            BindingResult bindingResult,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, errorMessage, null);
        }

        return new ResponseObject<>(
                HttpStatus.OK,
                "Success",
                paymentService.createVnPayPayment(request, paymentRequest.getOrderId(), paymentRequest.getBankCode())
        );
    }

    // Endpoint GET để xử lý callback từ VNPay
    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_TxnRef"); // Giả sử vnp_TxnRef chứa orderId

        if ("00".equals(status)) { // VNPay trả về "00" cho thành công
            // Cập nhật trạng thái đơn hàng thành đã thanh toán thành công
            orderService.updateOrderStatus(orderId, 3); // Giả sử có phương thức này
            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentDTO.VNPayResponse("00", "Success", ""));
        } else {
            // Cập nhật trạng thái đơn hàng thất bại hoặc cần xem xét
            orderService.updateOrderStatus(orderId, 1); // Giả sử có phương thức này
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }
}
