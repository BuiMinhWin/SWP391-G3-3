package com.example.demo.controller;


import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.service.iml.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @GetMapping("/getServices/{orderDetailId}")
    public ResponseEntity<String> getServices(@PathVariable String orderDetailId) {
        String servicesJson = servicesService.getServices(orderDetailId);
        return ResponseEntity.ok(servicesJson);
    }

    @PatchMapping("/updateService/{orderDetailId}/service/{serviceId}")
    public ResponseEntity<ServicesDTO> updateServiceStatusByOrderIdAndServiceId(
            @PathVariable String orderDetailId,
            @PathVariable Integer serviceId,
            @RequestParam String newStatus) {
        ServicesDTO updatedService = servicesService.updateServiceStatusByOrderIdAndServiceId(orderDetailId, serviceId, newStatus);
        return ResponseEntity.ok(updatedService);
    }
}