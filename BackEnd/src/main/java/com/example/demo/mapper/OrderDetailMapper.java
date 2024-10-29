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
                orderDetail.getStatus(),
                orderDetail.getTotalServicePrice(),
                orderDetail.getServiceId1(),
                orderDetail.getServiceId2(),
                orderDetail.getServiceId3(),
                orderDetail.getServicePrice1(),
                orderDetail.getServicePrice2(),
                orderDetail.getServicePrice3()
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
        orderDetail.setServiceId1(orderDetailDTO.getServiceId1());
        orderDetail.setServiceId2(orderDetailDTO.getServiceId2());
        orderDetail.setServiceId3(orderDetailDTO.getServiceId3());
        orderDetail.setServicePrice1(orderDetailDTO.getServicePrice1());
        orderDetail.setServicePrice2(orderDetailDTO.getServicePrice2());
        orderDetail.setServicePrice3(orderDetailDTO.getServicePrice3());
        return orderDetail;
    }
}
