package com.example.demo.mapper;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Services;

public class ServicesMapper {

    public static ServicesDTO mapToServicesDTO(Services service) {
        return new ServicesDTO(
                service.getServicesId(),
                service.getPrice(),
                service.getServicesStatus(),
                service.getServicesName()
        );
    }

    public static Services mapToServices(ServicesDTO servicesDTO) {
        Services service = new Services();
        service.setServicesId(servicesDTO.getServicesId());
        service.setPrice(servicesDTO.getPrice());
        service.setServicesStatus(servicesDTO.getServicesStatus());
        service.setServicesName(servicesDTO.getServicesName());
        return service;
    }
}
