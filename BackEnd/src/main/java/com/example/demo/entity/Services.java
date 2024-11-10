package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`services`")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "services_id")
    private Integer servicesId;

    @Column(name = "services_name")
    private String servicesName;

    @Column(name = "price")
    private int price;

    @Column(name = "services_status")
    private String servicesStatus;

    @ManyToMany(mappedBy = "services")
    private Set<Order> orders = new HashSet<>();
}
