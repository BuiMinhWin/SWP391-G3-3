package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDetailDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository,
                              OrderRepository orderRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderDetailDTO> createOrderDetail(List<OrderDetailDTO> orderDetailDTOs) {
        List<OrderDetailDTO> savedOrderDetails = new ArrayList<>();

        for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
            System.out.println("Received OrderDetailDTO: " + orderDetailDTO);

            Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDetailDTO.getOrderId()));

            OrderDetail orderDetail = OrderDetailMapper.mapToOrderDetail(orderDetailDTO, order);

            if (orderDetailDTO.getCreatedAt() == null) {
                orderDetail.setCreatedAt(LocalDateTime.now());
            }

            if (orderDetailDTO.getStatus() == 0) {
                orderDetail.setStatus(1);
            }

            System.out.println("Creating OrderDetail with Order ID: " + orderDetail.getOrder().getOrderId());

            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

            savedOrderDetails.add(OrderDetailMapper.mapToOrderDetailDTO(savedOrderDetail));
        }
        return savedOrderDetails;
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_OrderId(orderId);
        if (orderDetails.isEmpty()) {
            throw new ResourceNotFoundException("No OrderDetails found for order with id " + orderId);
        }

        return orderDetails.stream()
                .map(OrderDetailMapper::mapToOrderDetailDTO)
                .collect(Collectors.toList());
    }

    public OrderDetailDTO updateOrderDetail(String orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + orderDetailId));

        existingOrderDetail.setStatus(orderDetailDTO.getStatus());
        existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
        if (orderDetailDTO.getCreatedAt() == null) {
            orderDetailDTO.setCreatedAt(LocalDateTime.now());
        }

        OrderDetail updatedOrderDetail = orderDetailRepository.save(existingOrderDetail);

        return OrderDetailMapper.mapToOrderDetailDTO(updatedOrderDetail);
    }


}
