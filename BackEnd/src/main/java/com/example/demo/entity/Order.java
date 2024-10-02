package com.example.demo.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "`order`")
public class Order {
    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    private String accountId;
    private Date orderDate;
    private Date shippedDate;
    private String origin;
    private String destination;
    private String freight;
    private float totalPrice;
    private String serviceId;
    private String documentId;
    private int status;


}
