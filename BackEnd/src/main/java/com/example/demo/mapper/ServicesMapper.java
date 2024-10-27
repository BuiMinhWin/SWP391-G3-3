package com.example.demo.mapper;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;

public class ServicesMapper {

    public static ServicesDTO maptoServicesDTO(Services service) {
        return new ServicesDTO(
                service.getServicesId(),
                service.getOrderDetail().getOrderDetailId(),
                service.getPrice(),
                service.getServiceStatus(),
                service.getServicesName()
        );
    }

    public static Services mapToServices(ServicesDTO servicesDTO, OrderDetail orderDetail) {
        Services service = new Services();
        service.setServicesId(servicesDTO.getServicesId());
        service.setOrderDetail(orderDetail);
        service.setPrice(servicesDTO.getPrice());
        service.setServiceStatus(servicesDTO.getServiceStatus());
        service.setServicesName(servicesDTO.getServiceName());
        return service;
    }
}
