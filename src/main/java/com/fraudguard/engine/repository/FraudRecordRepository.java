package com.fraudguard.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fraudguard.engine.model.FraudRecord;

@Repository
public interface FraudRecordRepository extends JpaRepository<FraudRecord, Long> {
}