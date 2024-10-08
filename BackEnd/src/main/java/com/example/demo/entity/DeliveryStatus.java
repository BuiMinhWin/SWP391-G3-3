package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`deliveryStatus`")
public class DeliveryStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_status_id", columnDefinition = "CHAR(36)")
    private String deliveryStatusId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "time_tracking", nullable = false)
    private LocalDateTime timeTracking;

    @Column(name = "current_locate")
    private String currentLocate;

    @Column(name = "status")
    private int status;
}
