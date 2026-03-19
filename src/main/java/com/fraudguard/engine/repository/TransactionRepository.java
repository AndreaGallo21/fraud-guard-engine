package com.fraudguard.engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fraudguard.engine.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Esta es la "joya de la corona". Spring Boot entiende el nombre del método y
    // genera automáticamente el SQL para buscar la transacción más reciente de un usuario.
    Optional<Transaction> findFirstByUserIdOrderByTimestampDesc(String userId);
}