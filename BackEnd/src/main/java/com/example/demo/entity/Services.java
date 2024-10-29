package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`services`")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "services_id", columnDefinition = "CHAR(36)")
    private String servicesId;

    @Column(name = "services_name")
    private String servicesName;

    @Column(name = "price")
    private int price;

    @Column(name = "service_status")
    private String serviceStatus;

    @ManyToMany(mappedBy = "services")
    private Set<OrderDetail> orderDetails = new HashSet<>();
}
