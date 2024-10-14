package com.example.demo.service.iml;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

        int serviceId = servicesDTO.getServiceId();

        switch (serviceId) {
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
}

