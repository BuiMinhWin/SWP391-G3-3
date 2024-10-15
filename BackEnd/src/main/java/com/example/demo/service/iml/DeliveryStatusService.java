package com.example.demo.service.iml;


import com.example.demo.dto.request.DeliveryStatusDTO;
import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.DeliveryStatus;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DeliveryStatusMapper;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.repository.DeliveryStatusRepository;
import com.example.demo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeliveryStatusService {

    @Autowired
    private DeliveryStatusRepository deliveryStatusRepository;

    @Autowired
    private OrderRepository orderRepository;


    public DeliveryStatusDTO createDeliveryStatus(DeliveryStatusDTO deliveryStatusDTO) {

        Order order = orderRepository.findById(deliveryStatusDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryStatus not found with id " + deliveryStatusDTO.getOrderId()));

        DeliveryStatus deliveryStatus = DeliveryStatusMapper.mapToDeliveryStatus(deliveryStatusDTO, order);


        if (deliveryStatusDTO.getCurrentLocate() == null) {
            deliveryStatus.setCurrentLocate("");
        }

        if (deliveryStatusDTO.getTimeTracking() == null) {
            deliveryStatus.setTimeTracking(LocalDateTime.now());
        }

        deliveryStatus.setStatus(1);

        System.out.println("Creating DeliveryStatus with Order ID: " + deliveryStatus.getOrder().getOrderId());

        DeliveryStatus savedDeliveryStatus = deliveryStatusRepository.save(deliveryStatus);

        return DeliveryStatusMapper.maptoDeliveryStatusDTO(savedDeliveryStatus);
    }

    public List<DeliveryStatusDTO> getDeliveryStatusByOrderId(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<DeliveryStatus> deliveryStatus = deliveryStatusRepository.findByOrder(order);
        return deliveryStatus.stream()
                .map(DeliveryStatusMapper::maptoDeliveryStatusDTO)
                .collect(Collectors.toList());
    }

    public DeliveryStatusDTO updateDeliveryStatus(String deliveryStatusId, DeliveryStatusDTO updatedStatusDTO) {
        DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(deliveryStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryStatus not found with id " + deliveryStatusId));

        deliveryStatus.setStatus(updatedStatusDTO.getStatus());

        if (updatedStatusDTO.getCurrentLocate() != null) {
            deliveryStatus.setCurrentLocate(updatedStatusDTO.getCurrentLocate());
        }

        if (updatedStatusDTO.getTimeTracking() == null) {
            deliveryStatus.setTimeTracking(LocalDateTime.now());
        }

        DeliveryStatus updatedDeliveryStatus = deliveryStatusRepository.save(deliveryStatus);
        return DeliveryStatusMapper.maptoDeliveryStatusDTO(updatedDeliveryStatus);
    }

}
