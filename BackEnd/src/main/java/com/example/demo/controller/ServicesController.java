package com.example.demo.controller;


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

    @PutMapping("/{servicesId}/price")
    public ResponseEntity<String> updateServicePrice(
            @PathVariable String servicesId,
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
