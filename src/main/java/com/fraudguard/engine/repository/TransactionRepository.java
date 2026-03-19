package com.fraudguard.engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fraudguard.engine.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Buscamos la última transacción de un usuario específico para comparar tiempos/lugares
    Optional<Transaction> findFirstByUserIdOrderByTimestampDesc(String userId);
}