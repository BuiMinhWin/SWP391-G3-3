package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDetailDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderDetailService {

    @Autowired
    private  OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        System.out.println("Received OrderDetailDTO: " + orderDetailDTO);

        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + orderDetailDTO.getOrderId()));
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
}
