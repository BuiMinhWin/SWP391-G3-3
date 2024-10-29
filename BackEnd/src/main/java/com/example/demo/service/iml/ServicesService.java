package com.example.demo.service.iml;

import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.ServiceRepository;
import jakarta.transaction.Transactional;
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


    @Transactional
    public void updateServicePrice(String servicesId, int newPrice) {
        Services service = serviceRepository.findById(servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + servicesId));
        service.setPrice(newPrice);
        serviceRepository.save(service);
    }

    public List<Map<String, Object>> getAllService() {
        List<Services> servicesList = serviceRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Services service : servicesList) {
            Map<String, Object> serviceData = new ConcurrentHashMap<>();
            serviceData.put("servicesId", service.getServicesId());
            serviceData.put("servicesName", service.getServicesName());
            serviceData.put("price", service.getPrice());
            result.add(serviceData);
        }

        return result;
    }

}
