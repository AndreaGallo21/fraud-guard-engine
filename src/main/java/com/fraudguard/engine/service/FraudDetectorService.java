package com.fraudguard.engine.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fraudguard.engine.model.FraudRecord;
import com.fraudguard.engine.model.Transaction;
import com.fraudguard.engine.repository.FraudRecordRepository;

@Service
public class FraudDetectorService {

    @Autowired
    private FraudRecordRepository repository;

    public String analyzeTransaction(Transaction transaction) {
        String status = "APPROVED";
        String reason = "Normal transaction";
        String country = determineCountry(transaction.getLongitude());

        // 1. Regla de Monto Alto (Más de 10,000 soles)
        if (transaction.getAmount() > 10000) {
            status = "SUSPICIOUS";
            reason = "High amount threshold";
        }

        // 2. Regla de Ubicación (Simulación de Japón vs Perú)
        if (transaction.getLongitude() > 100) {
            status = "FRAUD";
            reason = "Impossible travel speed";
            country = "Japan";
        }

        // GUARDAR EN POSTGRESQL (Para Power BI)
        saveToDatabase(transaction, status, reason, country);

        return status;
    }

    private void saveToDatabase(Transaction t, String status, String reason, String country) {
        FraudRecord record = new FraudRecord();
        record.setUserId(t.getUserId());
        record.setAmount(t.getAmount());
        record.setStatus(status);
        record.setReason(reason);
        record.setCountry(country);
        record.setTimestamp(LocalDateTime.now());
        
        repository.save(record);
    }

    private String determineCountry(double longitude) {
        return (longitude > 100) ? "Japan" : "Peru";
    }
}