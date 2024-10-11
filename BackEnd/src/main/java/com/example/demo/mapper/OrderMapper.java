package com.example.demo.mapper;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.util.DistanceCalculator;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private static final int RATE_PER_KM = 15000;

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
                order.getStatus(),
                order.getOriginLatitude(),
                order.getOriginLongitude(),
                order.getDestinationLatitude(),
                order.getDestinationLongitude()
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
        order.setPostalCode(orderDTO.getPostalCode() != null ? orderDTO.getPostalCode() : 0);
        order.setReceiverNote(orderDTO.getReceiverNote());
        order.setSenderNote(orderDTO.getSenderNote());
        order.setOrderNote(orderDTO.getOrderNote());

        double distance = DistanceCalculator.calculateDistance(
                orderDTO.getOriginLatitude(),
                orderDTO.getOriginLongitude(),
                orderDTO.getDestinationLatitude(),
                orderDTO.getDestinationLongitude()
        );
        int calculatedTotalPrice = DistanceCalculator.calculateTotalPrice(distance, RATE_PER_KM);
        order.setTotalPrice(calculatedTotalPrice);

        order.setStatus(orderDTO.getStatus());

        order.setOriginLatitude(orderDTO.getOriginLatitude());
        order.setOriginLongitude(orderDTO.getOriginLongitude());
        order.setDestinationLatitude(orderDTO.getDestinationLatitude());
        order.setDestinationLongitude(orderDTO.getDestinationLongitude());

        return order;
    }
}
