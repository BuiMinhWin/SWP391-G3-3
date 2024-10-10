package com.example.demo.controller;


import com.example.demo.dto.request.ServicesDTO;
import com.example.demo.service.iml.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @PostMapping("/create")
    public ResponseEntity<ServicesDTO> createService(@RequestBody ServicesDTO servicesDTO) {
        ServicesDTO savedServices = servicesService.createService(servicesDTO);
        return new ResponseEntity<>(savedServices, HttpStatus.CREATED);
    }
}
