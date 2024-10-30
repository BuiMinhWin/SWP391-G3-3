package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Column(name = "service_ids")
    private String serviceIds;

    @ManyToMany
    @JoinTable(
            name = "order_detail_services",
            joinColumns = @JoinColumn(name = "order_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "services_id")
    )
    private Set<Services> services = new HashSet<>();

}
