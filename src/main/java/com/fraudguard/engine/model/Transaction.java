package com.fraudguard.engine.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Double amount;
    
    // El timestamp es CLAVE para calcular la velocidad entre dos compras
    private LocalDateTime timestamp;
    
    // Coordenadas para la regla de geolocalización
    private Double latitude;
    private Double longitude;

    private String merchantCategory; 
    private String status; // Aquí guardaremos 'APPROVED', 'FRAUD', etc.
}