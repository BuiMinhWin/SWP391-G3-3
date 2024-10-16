package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "`document`")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_id", columnDefinition = "CHAR(36)")
    private String documentId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_name")
    private String fileName;

}
