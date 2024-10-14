package com.example.demo.service.iml;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ServicesDTO createService(ServicesDTO servicesDTO) {

        Order order = orderRepository.findById(servicesDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Services not found with id " + servicesDTO.getOrderId()));

        int servicesId = servicesDTO.getServiceId();

        switch (servicesId) {
            case 1:
                if ("Yes".equalsIgnoreCase(servicesDTO.getServiceStatus())) {
                    servicesDTO.setPrice(30.0);
                } else {
                    servicesDTO.setPrice(0.0);
                }
                break;
            case 2:
                if ("Yes".equalsIgnoreCase(servicesDTO.getServiceStatus())) {
                    servicesDTO.setPrice(50.0);
                } else {
                    servicesDTO.setPrice(0.0);
                }
                break;
            case 3:
                if ("Yes".equalsIgnoreCase(servicesDTO.getServiceStatus())) {
                    servicesDTO.setPrice(70.0);
                } else {
                    servicesDTO.setPrice(0.0);
                }
                break;
            default:
                servicesDTO.setPrice(0.0);
                break;
        }

        Services service = ServicesMapper.mapToServices(servicesDTO, order);

        System.out.println("Creating Services with Order ID: " + service.getOrder().getOrderId());

        Services savedService = serviceRepository.save(service);

        return ServicesMapper.maptoServicesDTO(savedService);
    }

    public List<ServicesDTO> getServices(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<Services> services = serviceRepository.findByOrder(order);
        return services.stream()
                .map(ServicesMapper::maptoServicesDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServicesDTO updateServiceStatusByOrderIdAndServiceId(String orderId, Integer servicesId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Services service = serviceRepository.findByOrderAndServicesId(order, servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with orderId: " + orderId + " and serviceId: " + servicesId));

        if ("Yes".equalsIgnoreCase(newStatus) || "No".equalsIgnoreCase(newStatus)) {
            service.setServiceStatus(newStatus);

            switch (service.getServicesId()) {
                case 1:
                    service.setPrice("Yes".equalsIgnoreCase(newStatus) ? 30.0 : 0.0);
                    break;
                case 2:
                    service.setPrice("Yes".equalsIgnoreCase(newStatus) ? 50.0 : 0.0);
                    break;
                case 3:
                    service.setPrice("Yes".equalsIgnoreCase(newStatus) ? 70.0 : 0.0);
                    break;
                default:
                    service.setPrice(0.0);
                    break;
            }
        } else {
            throw new IllegalArgumentException("Invalid status. Must be 'Yes' or 'No'.");
        }

        Services updatedService = serviceRepository.save(service);

        return ServicesMapper.maptoServicesDTO(updatedService);
    }
}

