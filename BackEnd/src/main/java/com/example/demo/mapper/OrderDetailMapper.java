package com.example.demo.mapper;

import com.example.demo.dto.request.OrderDetailDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;

public class OrderDetailMapper {

    public static OrderDetailDTO mapToOrderDetailDTO(OrderDetail orderDetail) {
        return new OrderDetailDTO(
                orderDetail.getOrderDetailId(),
                orderDetail.getOrder().getOrderId(),
                orderDetail.getQuantity(),
                orderDetail.getWeight(),
                orderDetail.getDiscount(),
                orderDetail.getKoiType(),
                orderDetail.getCreatedAt(),
                orderDetail.getKoiName(),
                orderDetail.getStatus()
        );
    }

    public static OrderDetail mapToOrderDetail(OrderDetailDTO orderDetailDTO, Order order) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(orderDetailDTO.getOrderDetailId());
        orderDetail.setOrder(order);
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        orderDetail.setWeight(orderDetailDTO.getWeight());
        orderDetail.setDiscount(orderDetailDTO.getDiscount());
        orderDetail.setKoiType(orderDetailDTO.getKoiType());
        orderDetail.setCreatedAt(orderDetailDTO.getCreatedAt());
        orderDetail.setKoiName(orderDetailDTO.getKoiName());
        orderDetail.setStatus(orderDetailDTO.getStatus());
        return orderDetail;
    }
}
