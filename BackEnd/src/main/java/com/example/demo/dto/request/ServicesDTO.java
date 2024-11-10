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

    private Integer servicesId;
    private int price;
    private String servicesStatus;
    private String servicesName;
}
