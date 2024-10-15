package com.example.demo.mapper;

import com.example.demo.dto.request.DeliveryStatusDTO;
import com.example.demo.entity.DeliveryStatus;
import com.example.demo.entity.Order;

public class DeliveryStatusMapper {

    public static DeliveryStatusDTO maptoDeliveryStatusDTO(DeliveryStatus deliveryStatus) {
        return new DeliveryStatusDTO(
                deliveryStatus.getDeliveryStatusId(),
                deliveryStatus.getOrder().getOrderId(),
                deliveryStatus.getTimeTracking(),
                deliveryStatus.getCurrentLocate(),
                deliveryStatus.getStatus()
        );
    }

    public static DeliveryStatus mapToDeliveryStatus(DeliveryStatusDTO deliveryStatusDTO, Order order) {
        DeliveryStatus deliveryStatus = new DeliveryStatus();
        deliveryStatus.setDeliveryStatusId(deliveryStatusDTO.getDeliveryStatusId());
        deliveryStatus.setOrder(order);
        deliveryStatus.setTimeTracking(deliveryStatusDTO.getTimeTracking());
        deliveryStatus.setCurrentLocate(deliveryStatusDTO.getCurrentLocate());
        deliveryStatus.setStatus(deliveryStatusDTO.getStatus());
        return deliveryStatus;
    }
}
