package com.fraudguard.engine.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data // Esto genera getters, setters y toString automáticos gracias a Lombok
@Entity // Esto le dice a Java que esta clase es una tabla en la base de datos
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Double amount;
    private LocalDateTime timestamp;
    
    // Datos para la regla de geolocalización
    private Double latitude;
    private Double longitude;

    private String merchantCategory; // Ejemplo: 'Restaurante', 'Casino', 'Grifo'
    private String status; // 'PENDING', 'APPROVED', 'FRAUD', 'SUSPICIOUS'
}