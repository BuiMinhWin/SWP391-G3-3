package com.example.demo.service.iml;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.dto.request.ServiceDTO;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;
import com.example.demo.entity.Service;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.mapper.ServiceMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ServiceDTO createService(ServiceDTO serviceDTO) {

        Order order = orderRepository.findById(serviceDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceDTO.getOrderId()));

        Service service = ServiceMapper.mapToService(serviceDTO, order);

        System.out.println("Creating Service with Order ID: " + service.getOrder().getOrderId());

        Service savedService = serviceRepository.save(service);

        return ServiceMapper.maptoServiceDTO(savedService);
    }
}
