package com.example.demo.entity;


import com.example.demo.service.iml.ServicesService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`orderDetail`")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_detail_id", columnDefinition = "CHAR(36)")
    private String orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "weight")
    private float weight;

    @Column(name = "discount")
    private String discount;

    @Column(name = "koi_type")
    private String koiType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "koi_name")
    private String koiName;

    @Column(name = "status")
    private int status;

    @Column(name = "total_service_price", columnDefinition = "INTEGER DEFAULT 0")
    private int totalServicePrice;

    @Column(name = "service_id_1")
    private String serviceId1;

    @Column(name = "service_price_1")
    private int servicePrice1;

    @Column(name = "service_id_2")
    private String serviceId2;

    @Column(name = "service_price_2")
    private int servicePrice2;

    @Column(name = "service_id_3")
    private String serviceId3;

    @Column(name = "service_price_3")
    private int servicePrice3;

}
