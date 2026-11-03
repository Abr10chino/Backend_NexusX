package com.example.import_service.importation;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "imports")
@Data
public class ImportRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "total_rows", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalRows;

    @Column(name = "accepted_rows", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int acceptedRows;

    @Column(name = "rejected_rows", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int rejectedRows;

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @PrePersist
    protected void onCreate(){this.processedAt = LocalDateTime.now();}

}
