package com.example.demo.entity;

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
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID cho orderId
    @Column(name = "order_id", columnDefinition = "CHAR(36)")
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading cho Account
    @JoinColumn(name = "account_id", nullable = false) // Khóa ngoại
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Document> documents;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions;


    @Column(name = "status")
    private int status;

    @Column(name = "payment_status", nullable = false, columnDefinition = "BIT")
    private boolean paymentStatus = false;

//    @Column(name = "origin_latitude")
//    private double originLatitude;
//
//    @Column(name = "origin_longitude")
//    private double originLongitude;
//
//    @Column(name = "destination_latitude")
//    private double destinationLatitude;
//
//    @Column(name = "destination_longitude")
//    private double destinationLongitude;

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
}
