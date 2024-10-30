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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        this.serviceRepository = serviceRepository; // Khởi tạo biến
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

        if (orderDetailDTO.getServiceIds() != null && !orderDetailDTO.getServiceIds().isEmpty()) {
            String serviceIdsString = String.join(",", orderDetailDTO.getServiceIds());
            orderDetail.setServiceIds(serviceIdsString);
        }

        setServicePrices(orderDetailDTO, orderDetail);

        System.out.println("Creating OrderDetail with Order ID: " + orderDetail.getOrder().getOrderId());

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

        return OrderDetailMapper.mapToOrderDetailDTO(savedOrderDetail);
    }

    private void setServicePrices(OrderDetailDTO orderDetailDTO, OrderDetail orderDetail) {
        int totalServicePrice = 0;
        Set<Services> servicesSet = new HashSet<>();

        Set<String> requestedServiceIds = new HashSet<>();
        if (orderDetailDTO.getServiceIds() != null && !orderDetailDTO.getServiceIds().isEmpty()) {
            List<String> serviceIdsList = orderDetailDTO.getServiceIds();
            requestedServiceIds.addAll(serviceIdsList);
        }

        int servicePrice1 = 0;
        int servicePrice2 = 0;
        int servicePrice3 = 0;

        for (String serviceId : requestedServiceIds) {
            Services service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceId));
            servicesSet.add(service);

            switch (serviceId) {
                case "1":
                    servicePrice1 = service.getPrice();
                    break;
                case "2":
                    servicePrice2 = service.getPrice();
                    break;
                case "3":
                    servicePrice3 = service.getPrice();
                    break;
                default:
                    break;
            }

            totalServicePrice += service.getPrice();
        }

        if (!requestedServiceIds.contains("1")) {
            servicePrice1 = 0;
        }
        if (!requestedServiceIds.contains("2")) {
            servicePrice2 = 0;
        }
        if (!requestedServiceIds.contains("3")) {
            servicePrice3 = 0;
        }

        orderDetail.setServices(servicesSet);
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
