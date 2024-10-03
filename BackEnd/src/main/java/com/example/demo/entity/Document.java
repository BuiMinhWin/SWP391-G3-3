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

    @Column(name = "document_id", nullable = false)
    private String documentId;

    @Column(name = "document_type")
    private String document_type;

    @Column(name = "description")
    private String description;
}
