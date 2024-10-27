package com.example.demo.service.iml;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.ServiceRepository;
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

    @Transactional
    public List<ServicesDTO> createServicesForOrder(ServicesDTO servicesDTO) {

        OrderDetail orderDetail = orderDetailRepository.findById(servicesDTO.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + servicesDTO.getOrderDetailId()));

        List<ServicesDTO> servicesDTOList = new ArrayList<>();

        for (int serviceId = 1; serviceId <= 3; serviceId++) {
            ServicesDTO newServiceDTO = new ServicesDTO();
            newServiceDTO.setOrderDetailId(servicesDTO.getOrderDetailId());
            newServiceDTO.setServiceId(serviceId);
            newServiceDTO.setServiceStatus("No");
            newServiceDTO.setPrice(0.0);

            String serviceName;
            switch (serviceId) {
                case 1:
                    serviceName = "Service Type A";
                    break;
                case 2:
                    serviceName = "Service Type B";
                    break;
                case 3:
                    serviceName = "Service Type C";
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



    public List<ServicesDTO> getServices(String orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));

        List<Services> services = serviceRepository.findByOrderDetail(orderDetail);
        return services.stream()
                .map(ServicesMapper::maptoServicesDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServicesDTO updateServiceStatusByOrderIdAndServiceId(String orderDetailId, Integer servicesId, String newStatus) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));

        Services service = serviceRepository.findByOrderDetailAndServicesId(orderDetail, servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with orderId: " + orderDetailId + " and serviceId: " + servicesId));

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

