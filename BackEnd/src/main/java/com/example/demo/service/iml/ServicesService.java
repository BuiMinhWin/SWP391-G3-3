package com.example.demo.service.iml;

import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.entity.Services;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.repository.ServicesRepository;
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

    private final ServicesRepository servicesRepository;


    public ServicesDTO createService(ServicesDTO servicesDTO){

        Services services = ServicesMapper.mapToServices(servicesDTO);

        services.setServicesStatus("online");

        int price = servicesDTO.getPrice();
        if (price < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0.");
        }
        services.setPrice(price);

        Services saveService = servicesRepository.save(services);

        return ServicesMapper.mapToServicesDTO(saveService);
    }

    public void deactivateService(Integer servicesId) {
        Services services = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service does not exist with id: " + servicesId));
        services.setServicesStatus("offline");
        servicesRepository.save(services);
    }

    public void activateService(Integer servicesId) {
        Services services = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service does not exist with id: " + servicesId));
        services.setServicesStatus("online");
        servicesRepository.save(services);
    }

    @Transactional
    public void updateServicePrice(Integer servicesId, int newPrice) {
        Services service = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + servicesId));
        service.setPrice(newPrice);
        servicesRepository.save(service);
    }

    public List<Map<String, Object>> getAllService() {
        List<Services> servicesList = servicesRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Services service : servicesList) {
            Map<String, Object> serviceData = new ConcurrentHashMap<>();
            serviceData.put("servicesId", service.getServicesId());
            serviceData.put("servicesName", service.getServicesName());
            serviceData.put("price", service.getPrice());
            serviceData.put("servicesStatus", service.getServicesStatus());
            result.add(serviceData);
        }
        return result;
    }

}
