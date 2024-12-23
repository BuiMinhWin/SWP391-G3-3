package com.example.demo.mapper;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.entity.Services;
import com.example.demo.entity.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO mapToOrderDTO(Order order) {
        List<Integer> serviceIds = order.getServices().stream()
                .map(Services::getServicesId)
                .collect(Collectors.toList());

        int totalQuantity = order.getOrderDetails().stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();

        float totalWeight = (float) order.getOrderDetails().stream()
                .mapToDouble(OrderDetail::getWeight)
                .sum();


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
                order.getReceiverPhone(),
                order.getSenderPhone(),
                order.getSenderNote(),
                order.getReceiverNote(),
                order.getOrderNote(),
                order.getProvince(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getSale(),
                order.getDeliver(),
                order.getDistance(),
                order.getVnpTxnRef(),
                serviceIds,
                totalWeight,
                totalQuantity,
                order.getDiscount()
        );
    }

    public Order mapToOrder(OrderDTO orderDTO, Account account) {
        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setShippedDate(orderDTO.getShippedDate());
        order.setOrigin(orderDTO.getOrigin());
        order.setDestination(orderDTO.getDestination());
        order.setFreight(orderDTO.getFreight());
        order.setReceiverName(orderDTO.getReceiverName());
        order.setSenderName(orderDTO.getSenderName());
        order.setReceiverPhone(orderDTO.getReceiverPhone());
        order.setSenderPhone(orderDTO.getSenderPhone());
        order.setSenderNote(orderDTO.getSenderNote());
        order.setReceiverNote(orderDTO.getReceiverNote());
        order.setProvince(orderDTO.getProvince());
        order.setOrderNote(orderDTO.getOrderNote());
        order.setDeliver(orderDTO.getDeliver());
        order.setSale(orderDTO.getSale());
        order.setDistance(orderDTO.getDistance());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setStatus(orderDTO.getStatus());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setVnpTxnRef(orderDTO.getVnpTxnRef());
        order.setDiscount(orderDTO.getDiscount());

        return order;
    }

    public OrderDTO convertToDTO(Order order) {
        return mapToOrderDTO(order);
    }
}
