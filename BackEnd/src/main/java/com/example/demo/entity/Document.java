package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`document`")
public class Document {

    @Id
    @Column(name = "document_id", columnDefinition = "CHAR(36)")
    private String documentId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "document_id", referencedColumnName = "order_id")
    private Order order;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "description")
    private String description;
}
