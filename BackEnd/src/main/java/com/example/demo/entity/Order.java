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
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", columnDefinition = "CHAR(36)")
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipped_date")
    private LocalDateTime shippedDate;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "freight")
    private String freight;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    @Column(name = "sender_phone")
    private String senderPhone;

    @Column(name = "sender_note")
    private String senderNote;

    @Column(name = "receiver_note")
    private String receiverNote;

    @Column(name = "order_note")
    private String orderNote;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DeliveryStatus> deliveryStatus = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Feedback> feedBack = new HashSet<>();

    @Column(name = "status")
    private int status;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "distance")
    private double distance;

    @Column(name = "vnp_txn_ref", unique = true)
    private String vnpTxnRef;

    @Column(name = "province")
    private String province;

    @Column(name = "deliver")
    private String deliver;

    @Column(name = "sale")
    private String sale;

    public OrderDetail getOrderDetail() {
        if (orderDetails != null && !orderDetails.isEmpty()) {
            return orderDetails.iterator().next();
        }
        return null;
    }

    @Column(name = "service_ids")
    private String serviceIds;


    @Column(name = "discount")
    private String discount;

    @ManyToMany
    @JoinTable(
            name = "order_services",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "services_id")
    )
    private Set<Services> services = new HashSet<>();
}
