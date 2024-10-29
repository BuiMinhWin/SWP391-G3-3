package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDetailDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository,
                              OrderRepository orderRepository,
                              ServiceRepository serviceRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.serviceRepository = serviceRepository;
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

        setServicePrices(orderDetailDTO, orderDetail);

        System.out.println("Creating OrderDetail with Order ID: " + orderDetail.getOrder().getOrderId());

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

        return OrderDetailMapper.mapToOrderDetailDTO(savedOrderDetail);
    }

    private void setServicePrices(OrderDetailDTO orderDetailDTO, OrderDetail orderDetail) {
        int totalServicePrice = 0;

        if (orderDetailDTO.getServiceId1() != null && !orderDetailDTO.getServiceId1().isEmpty()) {
            orderDetail.setServiceId1(orderDetailDTO.getServiceId1());
            Services service1 = serviceRepository.findByServicesId(orderDetailDTO.getServiceId1())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + orderDetailDTO.getServiceId1()));
            orderDetail.setServicePrice1(service1.getPrice());
            totalServicePrice += service1.getPrice();
        } else {
            orderDetail.setServicePrice1(0);
        }

        if (orderDetailDTO.getServiceId2() != null && !orderDetailDTO.getServiceId2().isEmpty()) {
            orderDetail.setServiceId2(orderDetailDTO.getServiceId2());
            Services service2 = serviceRepository.findByServicesId(orderDetailDTO.getServiceId2())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + orderDetailDTO.getServiceId2()));
            orderDetail.setServicePrice2(service2.getPrice());
            totalServicePrice += service2.getPrice();
        } else {
            orderDetail.setServicePrice2(0);
        }

        if (orderDetailDTO.getServiceId3() != null && !orderDetailDTO.getServiceId3().isEmpty()) {
            orderDetail.setServiceId3(orderDetailDTO.getServiceId3());
            Services service3 = serviceRepository.findByServicesId(orderDetailDTO.getServiceId3())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + orderDetailDTO.getServiceId3()));
            orderDetail.setServicePrice3(service3.getPrice());
            totalServicePrice += service3.getPrice();
        } else {
            orderDetail.setServicePrice3(0);
        }

        orderDetail.setTotalServicePrice(totalServicePrice);
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
