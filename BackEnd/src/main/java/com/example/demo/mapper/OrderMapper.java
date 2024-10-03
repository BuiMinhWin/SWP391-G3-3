package com.example.demo.mapper;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Order;

public class OrderMapper {

    public static OrderDTO mapToOrderDTO(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getAccountId(),
                order.getOrderDate(),
                order.getShippedDate(),
                order.getOrigin(),
                order.getDestination(),
                order.getFreight(),
                order.getTotalPrice(),
                order.getServiceId(),
                order.getDocumentId(),
                order.getStatus()
        );
    }

    public static Order mapToOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setAccountId(orderDTO.getAccountId());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setShippedDate(orderDTO.getShippedDate());
        order.setOrigin(orderDTO.getOrigin());
        order.setDestination(orderDTO.getDestination());
        order.setFreight(orderDTO.getFreight());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setServiceId(orderDTO.getServiceId());
        order.setDocumentId(orderDTO.getDocumentId());
        order.setStatus(orderDTO.getStatus());
        return order;
    }
}
