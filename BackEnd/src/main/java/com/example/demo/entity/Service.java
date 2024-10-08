package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`service`")
public class Service {
    @Id

    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @Column(name = "service_name")
    private int serviceName;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;
}
