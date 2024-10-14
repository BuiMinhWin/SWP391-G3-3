package com.example.demo.mapper;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Services;

public class ServicesMapper {

    public static ServicesDTO maptoServicesDTO(Services service) {
        return new ServicesDTO(
                service.getServicesId(),
                service.getOrder().getOrderId(),
                service.getPrice(),
                service.getServiceStatus(),
                service.getServicesName()
        );
    }

    public static Services mapToServices(ServicesDTO servicesDTO, Order order) {
        Services service = new Services();
        service.setServicesId(servicesDTO.getServiceId());
        service.setOrder(order);
        service.setPrice(servicesDTO.getPrice());
        service.setServiceStatus(servicesDTO.getServiceStatus());
        service.setServicesName(servicesDTO.getServiceName());
        return service;
    }
}
