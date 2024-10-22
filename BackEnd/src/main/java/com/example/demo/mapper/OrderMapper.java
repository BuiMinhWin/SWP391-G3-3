package com.example.demo.mapper;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.util.DistanceCalculator;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private static final int RATE_PER_KM = 14000;

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
                order.getReceiverPhone(),
                order.getSenderPhone(),
                order.getPostalCode(),
                order.getSenderNote(),
                order.getReceiverNote(),
                order.getOrderNote(),
                order.getTotalPrice(),
                order.getStatus(),
                order.isPaymentStatus(),

//                order.getOriginLatitude(),
//                order.getOriginLongitude(),
//                order.getDestinationLatitude(),
//                order.getDestinationLongitude()
                order.getDistance(),
                order.getVnpTxnRef(),
                order.getProvince()
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
        order.setPostalCode(orderDTO.getPostalCode() != null ? orderDTO.getPostalCode() : 0);
        order.setSenderNote(orderDTO.getSenderNote());
        order.setReceiverNote(orderDTO.getReceiverNote());
        order.setOrderNote(orderDTO.getOrderNote());

//        double distance = DistanceCalculator.calculateDistance(
//                orderDTO.getOriginLatitude(),
//                orderDTO.getOriginLongitude(),
//                orderDTO.getDestinationLatitude(),
//                orderDTO.getDestinationLongitude()
//        );
        order.setDistance(orderDTO.getDistance());
        int calculatedTotalPrice = DistanceCalculator.calculateTotalPrice(orderDTO.getDistance(), RATE_PER_KM);
        order.setTotalPrice(calculatedTotalPrice);

        order.setStatus(orderDTO.getStatus());
        order.setPaymentStatus(orderDTO.isPaymentStatus());
        order.setVnpTxnRef(orderDTO.getVnpTxnRef());
        order.setProvince(orderDTO.getProvince());

//        order.setOriginLatitude(orderDTO.getOriginLatitude());
//        order.setOriginLongitude(orderDTO.getOriginLongitude());
//        order.setDestinationLatitude(orderDTO.getDestinationLatitude());
//        order.setDestinationLongitude(orderDTO.getDestinationLongitude());

        return order;
    }
    public OrderDTO convertToDTO(Order order) {
        return mapToOrderDTO(order);
    }
}
