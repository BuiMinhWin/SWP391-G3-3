package com.example.demo.service.iml;


import com.example.demo.dto.request.DeliveryStatusDTO;
import com.example.demo.entity.DeliveryStatus;
import com.example.demo.entity.Order;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DeliveryStatusMapper;
import com.example.demo.repository.DeliveryStatusRepository;
import com.example.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
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

        int status = deliveryStatusDTO.getStatus();
        if (status < 0 || status > 6) {
            throw new IllegalArgumentException("Status must be between 0 and 6.");
        }
        deliveryStatus.setStatus(status);

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

    @Transactional
    public DeliveryStatusDTO updateDeliveryStatus(String deliveryStatusId, DeliveryStatusDTO updatedStatusDTO) {
        DeliveryStatus originalDeliveryStatus = deliveryStatusRepository.findById(deliveryStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryStatus not found with id " + deliveryStatusId));

        DeliveryStatus newDeliveryStatus = new DeliveryStatus();
        newDeliveryStatus.setOrder(originalDeliveryStatus.getOrder()); //lấy orderId

        newDeliveryStatus.setStatus(updatedStatusDTO.getStatus());

        newDeliveryStatus.setCurrentLocate(
                updatedStatusDTO.getCurrentLocate() != null ? updatedStatusDTO.getCurrentLocate() : originalDeliveryStatus.getCurrentLocate()
        );

        newDeliveryStatus.setTimeTracking(
                updatedStatusDTO.getTimeTracking() != null ? updatedStatusDTO.getTimeTracking() : LocalDateTime.now()
        );

        DeliveryStatus savedNewDeliveryStatus = deliveryStatusRepository.save(newDeliveryStatus);

        return DeliveryStatusMapper.maptoDeliveryStatusDTO(savedNewDeliveryStatus);
    }
}
