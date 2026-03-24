package com.fraudguard.engine.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fraudguard.engine.model.FraudRecord;
import com.fraudguard.engine.repository.FraudRecordRepository;

@Service
public class ReportGeneratorService {

    @Autowired
    private FraudRecordRepository repository;

    public void generateDailyReport() {
        try {
            List<FraudRecord> records = repository.findAll();
            StringBuilder csv = new StringBuilder();
            
            // Cabecera para Power BI
            csv.append("ID,User,Amount,Country,Status,Reason,Timestamp\n");

            for (FraudRecord r : records) {
                csv.append(String.format("%d,%s,%.2f,%s,%s,%s,%s\n", 
                    r.getId(), 
                    r.getUserId(),
                    r.getAmount(), 
                    r.getCountry(), 
                    r.getStatus(), 
                    r.getReason(), 
                    r.getTimestamp()));
            }

            // Guarda el archivo en la raíz del proyecto
            Files.write(Paths.get("reporte_fraude_diario.csv"), csv.toString().getBytes());
            System.out.println("✅ Reporte CSV generado exitosamente.");
            
        } catch (IOException e) {
            System.err.println("❌ Error al generar el CSV: " + e.getMessage());
        }
    }
}