package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @Column(name = "services_name")
    private String servicesName;

    @Column(name = "price")
    private int price;

    @Column(name = "service_status")
    private String serviceStatus;

}
