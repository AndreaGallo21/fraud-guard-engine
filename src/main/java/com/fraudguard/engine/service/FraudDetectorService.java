package com.fraudguard.engine.service;

import org.springframework.stereotype.Service;

import com.fraudguard.engine.model.Transaction;

@Service // Esto le dice a Spring Boot que esta clase es el "Cerebro" (Lógica de negocio)
public class FraudDetectorService {

    // Constante: Velocidad máxima permitida en km/h (un avión comercial va a ~900)
    private static final double MAX_SPEED_KMH = 800.0;

    public String checkTransaction(Transaction currentTx, Transaction lastTx) {
        
        // REGLA 1: Velocidad de viaje imposible
        if (isTravelImpossible(currentTx, lastTx)) {
            return "FRAUD: Impossible travel speed";
        }

        // REGLA 2: Monto muy elevado (Ejemplo: mayor a 10,000 soles/dólares)
        if (currentTx.getAmount() > 10000) {
            return "SUSPICIOUS: High amount threshold";
        }

        return "APPROVED";
    }

    private boolean isTravelImpossible(Transaction current, Transaction last) {
        if (last == null) return false;

        // Calculamos la distancia entre las dos coordenadas (fórmula simplificada)
        double distance = calculateDistance(
            last.getLatitude(), last.getLongitude(),
            current.getLatitude(), current.getLongitude()
        );

        // Suponiendo que el tiempo entre transacciones se mide en horas
        // (Por ahora simularemos que pasaron 0.5 horas para la prueba)
        double hours = 0.5; 
        double speed = distance / hours;

        return speed > MAX_SPEED_KMH;
    }

    // Método auxiliar para calcular distancia entre dos puntos (Lat/Long)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        return dist * 60 * 1.1515 * 1.609344; // Convertir a Kilómetros
    }
}