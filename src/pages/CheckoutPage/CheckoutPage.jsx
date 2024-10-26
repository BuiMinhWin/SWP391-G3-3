import React, { useEffect, useState } from "react";
import { useLocation, Navigate, useNavigate } from "react-router-dom";
import {
  Typography,
  Box,
  Paper,
  Grid,
  Button,
  Stepper,
  Step,
  StepLabel,
  Divider,
} from "@mui/material";
import {
  order,
  orderDetail,
  cancelOrder,
  getOrderPDF,
} from "../../services/CustomerService";
import axios from "axios";

const REST_API_BANK_URL =
  "http://koideliverysystem.id.vn:8080/api/v1/payment/vn-pay";

const steps = ["Đang xử lí", "Thanh toán", "Đang vận chuyển", "Đã hoàn thành"];

const CheckoutPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const orderId = location.state?.orderId;

  const [orderData, setOrderData] = useState(null);
  const [orderDetailData, setOrderDetailData] = useState([]);
  const [error, setError] = useState(null);
  const [pdfUrl, setPdfUrl] = useState(null);

  useEffect(() => {
    const fetchOrderData = async () => {
      try {
        const fetchedOrder = await order(orderId);
        setOrderData(fetchedOrder);

        const fetchedOrderDetails = await orderDetail(orderId);
        setOrderDetailData(
          Array.isArray(fetchedOrderDetails) ? fetchedOrderDetails : []
        );
      } catch (err) {
        console.error("Error fetching order data:", err);
        setError(err.message);
      }
    };

    const fetchPDF = async () => {
      try {
        const response = await getOrderPDF(orderId);
        const blob = new Blob([response], { type: "application/pdf" });
        const url = URL.createObjectURL(blob);
        setPdfUrl(url);
      } catch (err) {
        console.error("Error fetching PDF:", err);
        setError("Unable to load PDF.");
      }
    };

    if (orderId) {
      fetchOrderData();
      fetchPDF();
    }
  }, [orderId]);

  const handleCancelOrder = async () => {
    try {
      await cancelOrder(orderId);
      alert("Order canceled successfully.");
      navigate("/");
    } catch (error) {
      console.error("Error canceling order:", error);
      alert("Failed to cancel order.");
    }
  };

  const handleProceedToPayment = async () => {
    try {
      console.log("Order ID:", orderId);

      const response = await axios.post(REST_API_BANK_URL, {
        orderId,
        bankCode: "NCB",
        returnUrl: "http://localhost:3000/payment-outcome",
      });

      console.log("Payment API Response:", response.data);

      const paymentUrl = response.data.data?.paymentUrl;
      console.log("Payment URL:", paymentUrl);

      if (paymentUrl) {
        // Store a flag in state to indicate payment status
        navigate("/payment-outcome", {
          state: { orderId, paymentStatus: "pending" },
        });
        window.location.href = paymentUrl;
      } else {
        throw new Error("Payment URL not found.");
      }
    } catch (error) {
      if (error.response) {
        console.error("API Error:", error.response.data);
        // If the payment fails
        navigate("/payment-outcome", {
          state: { orderId, paymentStatus: "failed" },
        });
      } else if (error.request) {
        console.error("No Response from API:", error.request);
        alert("Failed to proceed to payment.");
      } else {
        console.error("Unexpected Error:", error.message);
        alert("Failed to proceed to payment.");
      }
    }
  };

  if (!orderId) return <Navigate to="/" />;
  if (error) return <Typography color="error">Error: {error}</Typography>;
  if (!orderData) return <Typography>Đang tải...</Typography>;

  
  return (
    <Box sx={{ p: 4, bgcolor: "#f5f5f5", minHeight: "100vh" }}>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" align="center" gutterBottom>
          Xác nhận đơn hàng
        </Typography>

        {/* Stepper for Order Status */}
        <Stepper activeStep={orderData.status} alternativeLabel sx={{ mb: 4 }}>
          {steps.map((label, index) => (
            <Step key={index}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {/* Order Information */}
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Typography variant="h6" gutterBottom>
             Thông tin đơn hàng
            </Typography>
            <Divider sx={{ mb: 2 }} />
            <Typography>Mã đơn: {orderData.orderId}</Typography>
            <Typography>
              Ngày đặt đơn: {new Date(orderData.orderDate).toLocaleString()}
            </Typography>
            <Typography>Tổng giá: {orderData.totalPrice} VND</Typography>
            <Typography>Tình trạng đơn hàng: {steps[orderData.status]}</Typography>
            <Typography>Tình trạng thanh toán: {orderData.paymentStatus? "Đã thanh toán" : "Chưa thanh toán"} </Typography>
          </Grid>

          {/* PDF Preview */}
          <Grid item xs={12} md={6}>
            <Typography variant="h6" gutterBottom>
              Xem trước PDF
            </Typography>
            <Divider sx={{ mb: 2 }} />
            {pdfUrl ? (
              <iframe
                src={pdfUrl}
                width="100%"
                height="400px"
                style={{ border: "1px solid #ccc", borderRadius: 4 }}
                title="Order PDF Preview"
              />
            ) : (
              <Typography>PDF đang được tải...</Typography>
            )}
          </Grid>

          {/* Order Details */}
          <Grid item xs={12}>
            <Typography variant="h6" gutterBottom>
              Chi tiết đơn hàng
            </Typography>
            <Divider sx={{ mb: 2 }} />
            {orderDetailData.length > 0 ? (
              <Box
                sx={{
                  maxHeight: 300,
                  overflow: "auto",
                  p: 1,
                  bgcolor: "#fafafa",
                  borderRadius: 1,
                }}
              >
                {orderDetailData.map((detail) => (
                  <Paper key={detail.orderDetailId} sx={{ mb: 2, p: 2 }}>
                    <Typography>Loại cá: {detail.koiType}</Typography>
                    <Typography>Biến thể: {detail.koiName}</Typography>
                    <Typography>Số lượng: {detail.quantity}</Typography>
                    <Typography>Cân nặng: {detail.weight} kg</Typography>
                    <Typography>Mã giảm giá: {detail.discount}</Typography>
                    <Typography>
                      Tình trạng cá: {detail.status === 0 ? "Khỏe mạnh" : "Bất thường"}
                    </Typography>
                  </Paper>
                ))}
              </Box>
            ) : (
              <Typography>Không có đơn nào được tìm thấy.</Typography>
            )}
          </Grid>

          {(orderData.status === 0 || orderData.status === 1) && (
            <Button
              variant="contained"
              color="error"
              onClick={handleCancelOrder}
              sx={{ mr: 2 }}
            >
              Hủy đơn
            </Button>
          )}
          {orderData.status === 1 && (
            <Button
              variant="contained"
              color="primary"
              onClick={handleProceedToPayment}
            >
              Thanh toán
            </Button>
          )}
        </Grid>
      </Paper>
    </Box>
  );
};

export default CheckoutPage;