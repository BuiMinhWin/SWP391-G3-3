package com.example.demo.service.iml;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ServiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<ServicesDTO> createServicesForOrder(ServicesDTO servicesDTO) {

        OrderDetail orderDetail = orderDetailRepository.findById(servicesDTO.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + servicesDTO.getOrderDetailId()));

        Order order = orderDetail.getOrder();
        if (order == null) {
            throw new ResourceNotFoundException("Order not found for OrderDetail with id: " + servicesDTO.getOrderDetailId());
        }

        List<ServicesDTO> servicesDTOList = new ArrayList<>();

        for (int servicesId = 1; servicesId <= 3; servicesId++) {
            ServicesDTO newServiceDTO = new ServicesDTO();
            newServiceDTO.setOrderDetailId(servicesDTO.getOrderDetailId());
            newServiceDTO.setServicesId(servicesId);
            newServiceDTO.setServiceStatus("No");
            newServiceDTO.setPrice(0);

            String serviceName;
            switch (servicesId) {
                case 1:
                    serviceName = "Bảo hiểm cho cá";
                    break;
                case 2:
                    serviceName = "Chăm sóc cá";
                    break;
                case 3:
                    serviceName = "Trả sau";
                    break;
                default:
                    serviceName = "Unknown Service";
                    break;
            }
            newServiceDTO.setServiceName(serviceName);

            Services service = ServicesMapper.mapToServices(newServiceDTO, orderDetail);
            Services savedService = serviceRepository.save(service);

            servicesDTOList.add(ServicesMapper.maptoServicesDTO(savedService));
        }

        return servicesDTOList;
    }



    public String getServices(String orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));

        List<Services> services = serviceRepository.findByOrderDetail(orderDetail);

        return services.stream()
                .map(ServicesMapper::maptoServicesDTO)
                .map(serviceDTO -> {
                    try {
                        return new ObjectMapper().writeValueAsString(serviceDTO);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error converting to JSON", e);
                    }
                })
                .collect(Collectors.joining(",\n"));
    }


    public ServicesDTO updateServiceStatusByOrderIdAndServiceId(String orderDetailId, Integer servicesId, String newStatus) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));

        Order order = orderDetail.getOrder();
        if (order == null) {
            throw new ResourceNotFoundException("Order not found for OrderDetail with id: " + orderDetailId);
        }

        Services service = serviceRepository.findByOrderDetailAndServicesId(orderDetail, servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with orderId: " + orderDetailId + " and serviceId: " + servicesId));

        if ("Yes".equalsIgnoreCase(newStatus) || "No".equalsIgnoreCase(newStatus)) {
            service.setServiceStatus(newStatus);

            int price;
            switch (service.getServicesId()) {
                case 1:
                    price = "Yes".equalsIgnoreCase(newStatus) ? 30 : 0;
                    break;
                case 2:
                    price = "Yes".equalsIgnoreCase(newStatus) ? 50 : 0;
                    break;
                case 3:
                    price = 0;
                    break;
                default:
                    price = 0;
                    break;
            }

            service.setPrice(price);

            if ("Yes".equalsIgnoreCase(newStatus)) {
                order.setTotalPrice(order.getTotalPrice() + price);
            } else {
                order.setTotalPrice(order.getTotalPrice() - price);
            }

            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Invalid status. Must be 'Yes' or 'No'.");
        }

        Services updatedService = serviceRepository.save(service);

        return ServicesMapper.maptoServicesDTO(updatedService);
    }

}
