package com.example.demo.controller;


import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.service.iml.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @GetMapping("/getServices/{orderDetailId}/{servicesName}")
    public ResponseEntity<ServicesDTO> getService(
            @PathVariable String orderDetailId,
            @RequestParam String servicesName) {
        ServicesDTO serviceDTO = servicesService.getService(orderDetailId, servicesName);
        return ResponseEntity.ok(serviceDTO);
    }

    @GetMapping("/Services")
    public ResponseEntity<List<Map<String, Object>>> getAllServiceNamesAndPrices() {
        List<Map<String, Object>> services = servicesService.getAllServiceNamesAndPrices();
        return ResponseEntity.ok(services);
    }

    @PatchMapping("/{orderDetailId}/{servicesName}/updateStatus")
    public ResponseEntity<ServicesDTO> updateServiceStatusByOrderIdAndServiceName(
            @PathVariable String orderDetailId,
            @PathVariable String servicesName,
            @RequestParam String newStatus) {
        ServicesDTO updatedService = servicesService.updateServiceStatusByOrderIdAndServiceName(orderDetailId, servicesName, newStatus);
        return ResponseEntity.ok(updatedService);
    }

    @PutMapping("/updatePrice/{servicesName}")
    public ResponseEntity<String> updateServicePrice(
            @PathVariable String servicesName,
            @RequestParam int newPrice) {
        servicesService.updateServicePrice(servicesName, newPrice);
        return ResponseEntity.ok("Service price updated successfully.");
    }
}
