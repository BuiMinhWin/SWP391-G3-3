package com.example.demo.mapper;

import com.example.demo.dto.request.ServiceDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Service;

public class ServiceMapper {

    public static ServiceDTO maptoServiceDTO(Service service) {
        return new ServiceDTO(
                service.getServiceId(),
                service.getOrder().getOrderId(),
                service.getPrice(),
                service.getDescription(),
                service.getServiceName()
        );
    }

    public static Service mapToService(ServiceDTO serviceDTO, Order order) {
        Service service = new Service();
        service.setServiceId(serviceDTO.getServiceId());
        service.setOrder(order);
        service.setPrice(serviceDTO.getPrice());
        service.setDescription(serviceDTO.getDescription());
        service.setServiceName(serviceDTO.getServiceName());
        return service;
    }
}
