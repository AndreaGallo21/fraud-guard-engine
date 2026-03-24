package com.fraudguard.engine.controller;

import com.fraudguard.engine.model.Transaction;
import com.fraudguard.engine.service.FraudDetectorService;
import com.fraudguard.engine.service.ReportGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private FraudDetectorService fraudService;

    @Autowired
    private ReportGeneratorService reportService;

    @PostMapping("/check")
    public String checkTransaction(@RequestBody Transaction transaction) {
        // Importante: Verifica que en FraudDetectorService el método se llame analyzeTransaction
        return fraudService.analyzeTransaction(transaction);
    }

    @GetMapping("/report")
    public String generateReport() {
        reportService.generateDailyReport();
        return "✅ ¡Éxito! El archivo 'reporte_fraude_diario.csv' ha sido creado en la carpeta de tu proyecto.";
    }
}