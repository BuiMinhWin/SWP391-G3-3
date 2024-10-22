package com.example.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicesDTO {

    private int serviceId;
    private String orderId;
    private double price;
    private String serviceStatus;
    private String serviceName;
}