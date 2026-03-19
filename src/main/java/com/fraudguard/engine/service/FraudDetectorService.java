package com.fraudguard.engine.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fraudguard.engine.model.Transaction;
import com.fraudguard.engine.repository.TransactionRepository;

@Service
public class FraudDetectorService {

    @Autowired
    private TransactionRepository repository; // Esto permite hablar con la base de datos

    private static final double MAX_SPEED_KMH = 800.0;

    public String checkTransaction(Transaction currentTx) {
        // 1. Buscamos la última transacción de este usuario en la DB
        var lastTxOpt = repository.findFirstByUserIdOrderByTimestampDesc(currentTx.getUserId());
        
        String result = "APPROVED";

        if (lastTxOpt.isPresent()) {
            Transaction lastTx = lastTxOpt.get();
            
            // REGLA 1: Velocidad de viaje imposible
            if (isTravelImpossible(currentTx, lastTx)) {
                result = "FRAUD: Impossible travel speed";
            }
        }

        // REGLA 2: Monto elevado
        if (currentTx.getAmount() > 10000) {
            result = "SUSPICIOUS: High amount threshold";
        }

        // 2. Guardamos la transacción actual para que sirva de "pasada" en la siguiente
        currentTx.setStatus(result);
        currentTx.setTimestamp(LocalDateTime.now()); // Le ponemos la hora exacta de ahora
        repository.save(currentTx);

        return result;
    }

    private boolean isTravelImpossible(Transaction current, Transaction last) {
        double distance = calculateDistance(
            last.getLatitude(), last.getLongitude(),
            current.getLatitude(), current.getLongitude()
        );

        // Si es la misma ubicación, no calculamos velocidad
        if (distance < 1.0) return false;

        // Calculamos el tiempo real que pasó entre la compra anterior y esta
        long seconds = Duration.between(last.getTimestamp(), LocalDateTime.now()).getSeconds();
        
        // Evitamos división por cero si las mandas al mismo milisegundo
        double hours = Math.max(seconds, 1) / 3600.0; 
        
        double speed = distance / hours;

        // Esto imprimirá en tu consola de VS Code para que veas el cálculo
        System.out.println("DEBUG -> Distancia: " + distance + " km | Tiempo: " + seconds + "s | Velocidad: " + speed + "km/h");

        return speed > MAX_SPEED_KMH;
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