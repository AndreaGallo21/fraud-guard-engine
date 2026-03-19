package com.fraudguard.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fraudguard.engine.model.Transaction;
import com.fraudguard.engine.service.FraudDetectorService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private FraudDetectorService fraudDetectorService;

    @PostMapping("/check")
    public String check(@RequestBody Transaction transaction) {
        // Por ahora enviamos 'null' como segunda transacción para la prueba inicial
        return fraudDetectorService.checkTransaction(transaction, null);
    }
}