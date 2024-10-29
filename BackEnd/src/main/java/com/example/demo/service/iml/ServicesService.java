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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServiceRepository serviceRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final Map<String, Integer> servicePrices = new ConcurrentHashMap<>();

    {
        servicePrices.put("Bảo hiểm cho cá", 30);
        servicePrices.put("Chăm sóc cá", 50);
        servicePrices.put("Trả sau", 0);
    }

    public List<ServicesDTO> createServicesForOrder(ServicesDTO servicesDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(servicesDTO.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderDetail not found with id " + servicesDTO.getOrderDetailId()));

        List<ServicesDTO> servicesDTOList = new ArrayList<>();

        for (String serviceName : servicePrices.keySet()) {
            ServicesDTO newServiceDTO = new ServicesDTO();
            newServiceDTO.setServicesName(serviceName);
            newServiceDTO.setServiceStatus("Yes");
            newServiceDTO.setPrice(servicePrices.get(serviceName));

            Services service = ServicesMapper.mapToServices(newServiceDTO, orderDetail);

            Services savedService = serviceRepository.save(service);
            servicesDTOList.add(ServicesMapper.maptoServicesDTO(savedService));
        }

        return servicesDTOList;
    }

    public void updateServicePrice(String serviceName, int newPrice) {
        if (!servicePrices.containsKey(serviceName)) {
            throw new ResourceNotFoundException("Service not found: " + serviceName);
        }
        servicePrices.put(serviceName, newPrice);
    }


    public ServicesDTO getService(String orderDetailId, String servicesName) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderDetail not found with id: " + orderDetailId));

        Services service = serviceRepository.findByOrderDetailAndServicesName(orderDetail, servicesName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with orderId: " + orderDetailId + " and serviceName: " + servicesName));

        return ServicesMapper.maptoServicesDTO(service);
    }

    public List<Map<String, Object>> getAllServiceNamesAndPrices() {
        List<Map<String, Object>> servicesList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : servicePrices.entrySet()) {
            Map<String, Object> serviceData = new ConcurrentHashMap<>();
            serviceData.put("serviceName", entry.getKey());
            serviceData.put("price", entry.getValue());
            servicesList.add(serviceData);
        }

        return servicesList;
    }


    public ServicesDTO updateServiceStatusByOrderIdAndServiceName(
            String orderDetailId, String servicesName, String newStatus) {

        if (!"Yes".equalsIgnoreCase(newStatus) && !"No".equalsIgnoreCase(newStatus)) {
            throw new IllegalArgumentException("Invalid status. Must be 'Yes' or 'No'.");
        }

        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderDetail not found with id: " + orderDetailId));

        Services service = serviceRepository.findByOrderDetailAndServicesName(orderDetail, servicesName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with orderId: " + orderDetailId + " and serviceName: " + servicesName));

        service.setServiceStatus(newStatus);

        if ("Yes".equalsIgnoreCase(newStatus)) {
            Integer updatedPrice = servicePrices.get(servicesName);
            if (updatedPrice == null) {
                throw new ResourceNotFoundException("Price not found for service: " + servicesName);
            }
            service.setPrice(updatedPrice);
        } else {
            service.setPrice(0.0);
        }

        Services updatedService = serviceRepository.save(service);

        return ServicesMapper.maptoServicesDTO(updatedService);
    }

}
