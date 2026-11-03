package com.example.import_service.importation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRecordRepository extends JpaRepository<ImportRecord, Long> {
}
