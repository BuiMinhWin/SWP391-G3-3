package com.example.demo.controller;


import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.iml.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create")
    public ResponseEntity<ServicesDTO> createService(@RequestBody ServicesDTO servicesDTO) {
        try {
            ServicesDTO createdService = servicesService.createService(servicesDTO);
            return new ResponseEntity<>(createdService, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{serviceId}/deactivate")
    public ResponseEntity<String> deactivateService(@PathVariable Integer serviceId) {
        try {
            servicesService.deactivateService(serviceId);
            return new ResponseEntity<>("Service deactivated successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{serviceId}/activate")
    public ResponseEntity<String> activateService(@PathVariable Integer serviceId) {
        try {
            servicesService.activateService(serviceId);
            return new ResponseEntity<>("Service activated successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{servicesId}/price")
    public ResponseEntity<String> updateServicePrice(
            @PathVariable Integer servicesId,
            @RequestParam int newPrice) {
        servicesService.updateServicePrice(servicesId, newPrice);
        return ResponseEntity.ok("Service price updated successfully.");
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllServices() {
        List<Map<String, Object>> services = servicesService.getAllService();
        return ResponseEntity.ok(services);
    }
}
