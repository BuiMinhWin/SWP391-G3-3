package com.example.demo.controller;


import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.service.iml.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @GetMapping("/getServices/{orderDetailId}/{servicesId}")
    public ResponseEntity<ServicesDTO> getService(
            @PathVariable String orderDetailId,
            @PathVariable String servicesId) {
        ServicesDTO serviceDTO = servicesService.getService(orderDetailId, servicesId);
        return ResponseEntity.ok(serviceDTO);
    }

    @PatchMapping("/updateService/{orderDetailId}/service/{serviceId}")
    public ResponseEntity<ServicesDTO> updateServiceStatusByOrderIdAndServiceId(
            @PathVariable String orderDetailId,
            @PathVariable String serviceId,
            @RequestParam String newStatus) {
        ServicesDTO updatedService = servicesService.updateServiceStatusByOrderIdAndServiceId(orderDetailId, serviceId, newStatus);
        return ResponseEntity.ok(updatedService);
    }

    @PutMapping("/{servicesId}/price")
    public ResponseEntity<String> updateServicePrice(
            @PathVariable String servicesName,
            @RequestParam int newPrice) {
        servicesService.updateServicePrice(servicesName, newPrice);
        return ResponseEntity.ok("Service price updated successfully.");
    }
}
