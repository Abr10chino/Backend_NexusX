package com.example.import_service.importationError;

import com.example.import_service.importation.ImportRecord;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "import_errors")
@Data
public class ImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "import_id", nullable = false)
    private ImportRecord importRecord;

    @Column(name="row_number", nullable = false)
    private int rowNumber;

    @Column(name="raw_data", nullable = false, columnDefinition = "TEXT")
    private String rawData;

    @Column(name="reason", nullable = false)
    private String reason;

}
