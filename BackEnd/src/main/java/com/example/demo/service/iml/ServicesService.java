package com.example.demo.service.iml;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServiceRepository serviceRepository;
    private final OrderDetailRepository orderDetailRepository;

    public List<ServicesDTO> createServicesForOrder(ServicesDTO servicesDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(servicesDTO.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderDetail not found with id " + servicesDTO.getOrderDetailId()));

        List<ServicesDTO> servicesDTOList = new ArrayList<>();

        // Define the names of the services
        String[] serviceNames = {"Bảo hiểm cho cá", "Chăm sóc cá", "Trả sau"};

        for (String serviceName : serviceNames) {
            ServicesDTO newServiceDTO = new ServicesDTO();
            newServiceDTO.setServiceName(serviceName);  // Assuming ServicesDTO has a setName method

            Services service = ServicesMapper.mapToServices(newServiceDTO, orderDetail);
            Services savedService = serviceRepository.save(service);

            servicesDTOList.add(ServicesMapper.maptoServicesDTO(savedService));
        }

        return servicesDTOList;
    }

    public void updateServicePrice(String serviceName, double newPrice) {
        Services service = serviceRepository.findByServicesName(serviceName)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with name: " + serviceName));
        service.setPrice(newPrice);
        serviceRepository.save(service);
    }

    public ServicesDTO getService(String orderDetailId, String servicesId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderDetail not found with id: " + orderDetailId));

        Services service = serviceRepository.findByOrderDetailAndServicesId(orderDetail, servicesId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with orderId: " + orderDetailId + " and serviceId: " + servicesId));

        return ServicesMapper.maptoServicesDTO(service);
    }

    public ServicesDTO updateServiceStatusByOrderIdAndServiceId(
            String orderDetailId, String servicesId, String newStatus) {

        if (!"Yes".equalsIgnoreCase(newStatus) && !"No".equalsIgnoreCase(newStatus)) {
            throw new IllegalArgumentException("Invalid status. Must be 'Yes' or 'No'.");
        }

        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderDetail not found with id: " + orderDetailId));

        Services service = serviceRepository.findByOrderDetailAndServicesId(orderDetail, servicesId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with orderId: " + orderDetailId + " and serviceId: " + servicesId));

        service.setServiceStatus(newStatus);
        service.setPrice("Yes".equalsIgnoreCase(newStatus) ? service.getPrice() : 0.0);

        Services updatedService = serviceRepository.save(service);
        return ServicesMapper.maptoServicesDTO(updatedService);
    }

}
