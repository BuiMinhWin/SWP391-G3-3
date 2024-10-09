package com.example.demo.mapper;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO mapToOrderDTO(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getAccount().getAccountId(),
                order.getOrderDate(),
                order.getShippedDate(),
                order.getOrigin(),
                order.getDestination(),
                order.getFreight(),
                order.getReceiverName(),
                order.getSenderName(),
                order.getReceiverAddress(),
                order.getSenderAddress(),
                order.getReceiverPhone(),
                order.getSenderPhone(),
                order.getPostalCode(),
                order.getReceiverNote(),
                order.getSenderNote(),
                order.getOrderNote(),
                order.getTotalPrice(),
                order.getStatus()
        );
    }

    public Order mapToOrder(OrderDTO orderDTO, Account account) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setAccount(account);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setShippedDate(orderDTO.getShippedDate());
        order.setOrigin(orderDTO.getOrigin());
        order.setDestination(orderDTO.getDestination());
        order.setFreight(orderDTO.getFreight());
        order.setReceiverName(orderDTO.getReceiverName());
        order.setSenderName(orderDTO.getSenderName());
        order.setReceiverAddress(orderDTO.getReceiverAddress());
        order.setSenderAddress(orderDTO.getSenderAddress());
        order.setReceiverPhone(orderDTO.getReceiverPhone());
        order.setSenderPhone(orderDTO.getSenderPhone());
        order.setPostalCode(orderDTO.getPostalCode());
        order.setReceiverNote(orderDTO.getReceiverNote());
        order.setSenderNote(orderDTO.getSenderNote());
        order.setOrderNote(orderDTO.getOrderNote());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setStatus(orderDTO.getStatus());
        return order;
    }
}
