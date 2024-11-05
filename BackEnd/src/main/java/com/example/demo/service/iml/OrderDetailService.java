package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDetailDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ServicesRepository;
import com.example.demo.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ServicesRepository servicesRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository,
                              OrderRepository orderRepository,
                              ServicesRepository servicesRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.servicesRepository = servicesRepository; // Khởi tạo biến
    }

    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
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

        return OrderDetailMapper.mapToOrderDetailDTO(savedOrderDetail);
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

}
