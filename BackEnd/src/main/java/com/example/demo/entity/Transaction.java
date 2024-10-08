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
@Table(name = "`transaction`")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", columnDefinition = "CHAR(36)")
    private String transactionId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "payment_method")
    private String freight;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

}
