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
@Table(name = "`orderDetail`")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_detail_id", columnDefinition = "CHAR(36)")
    private String orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Document document;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "weight")
    private float weight;

    @Column(name = "koi_type")
    private String koiType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "koi_name")
    private String koiName;

    @Column(name = "status")
    private int status;

}
