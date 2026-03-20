package com.fraudguard.engine.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fraudguard.engine.model.FraudRecord;
import com.fraudguard.engine.model.Transaction;
import com.fraudguard.engine.repository.FraudRecordRepository;
import com.fraudguard.engine.repository.TransactionRepository;

@Service
public class FraudDetectorService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private FraudRecordRepository recordRepository; // Nuevo: Para el Dashboard

    private static final double MAX_SPEED_KMH = 800.0;

    public String checkTransaction(Transaction currentTx) {
        var lastTxOpt = repository.findFirstByUserIdOrderByTimestampDesc(currentTx.getUserId());
        String result = "APPROVED";
        String reason = "Normal transaction";

        if (lastTxOpt.isPresent()) {
            if (isTravelImpossible(currentTx, lastTxOpt.get())) {
                result = "FRAUD";
                reason = "Impossible travel speed";
            }
        }

        if (currentTx.getAmount() > 10000) {
            result = "SUSPICIOUS";
            reason = "High amount threshold";
        }

        // Guardamos la transacción actual para el motor
        currentTx.setStatus(result);
        currentTx.setTimestamp(LocalDateTime.now());
        repository.save(currentTx);

        // NUEVO: Guardamos el registro histórico para BI
        // Por ahora simulamos "Peru" o "Japan" según la latitud para el mapa
        String country = currentTx.getLatitude() > 30 ? "Japan" : "Peru";
        recordRepository.save(new FraudRecord(currentTx.getUserId(), currentTx.getAmount(), result, reason, country));

        return result;
    }

    private boolean isTravelImpossible(Transaction current, Transaction last) {
        double distance = calculateDistance(last.getLatitude(), last.getLongitude(), current.getLatitude(), current.getLongitude());
        if (distance < 1.0) return false;
        long seconds = Duration.between(last.getTimestamp(), LocalDateTime.now()).getSeconds();
        double hours = Math.max(seconds, 1) / 3600.0; 
        return (distance / hours) > MAX_SPEED_KMH;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        return dist * 60 * 1.1515 * 1.609344;
    }
}