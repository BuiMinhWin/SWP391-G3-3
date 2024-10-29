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

    private String servicesId;
    private String orderDetailId;
    private int price;
    private String serviceStatus;
    private String servicesName;
}
