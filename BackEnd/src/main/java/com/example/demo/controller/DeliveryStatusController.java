package com.example.demo.controller;

import com.example.demo.dto.request.DeliveryStatusDTO;
import com.example.demo.service.iml.DeliveryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveryStatus")
public class DeliveryStatusController {

    @Autowired
    private DeliveryStatusService deliveryStatusService;

    @PostMapping("/create")
    public ResponseEntity<DeliveryStatusDTO> createFeedback(@RequestBody DeliveryStatusDTO deliveryStatusDTO) {
        DeliveryStatusDTO savedDeliveryStatus = deliveryStatusService.createDeliveryStatus(deliveryStatusDTO);
        return new ResponseEntity<>(savedDeliveryStatus, HttpStatus.CREATED);
    }

    @GetMapping("/getAllDeliveryStatusByOrderId/{orderId}")
    public ResponseEntity<List<DeliveryStatusDTO>> getDeliveryStatusByOrderId(@PathVariable String orderId) {
        List<DeliveryStatusDTO> deliveryStatus = deliveryStatusService.getDeliveryStatusByOrderId(orderId);
        return new ResponseEntity<>(deliveryStatus, HttpStatus.OK);
    }

    @PatchMapping("/{deliveryStatusId}")
    public ResponseEntity<DeliveryStatusDTO> updateDeliveryStatus(
            @PathVariable String deliveryStatusId,
            @RequestBody DeliveryStatusDTO updatedStatusDTO) {

        DeliveryStatusDTO updatedDeliveryStatus = deliveryStatusService.updateDeliveryStatus(deliveryStatusId, updatedStatusDTO);
        return ResponseEntity.ok(updatedDeliveryStatus);
    }
}
