package com.example.demo.entity;


import jakarta.mail.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Services> koiServices;

}
