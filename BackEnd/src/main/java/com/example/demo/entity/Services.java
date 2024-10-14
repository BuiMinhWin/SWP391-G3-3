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
@Table(name = "`services`")
public class Services {
    @Id
    @Column(name = "services_id", columnDefinition = "CHAR(36)")
    private int servicesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "services_name")
    private String servicesName;

    @Column(name = "price")
    private double price;

    @Column(name = "service_status")
    private String serviceStatus;
}
