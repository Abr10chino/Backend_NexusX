package com.example.import_service.importationError;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportErrorRepository extends JpaRepository<ImportError, Long> {
}
