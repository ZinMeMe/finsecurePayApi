package com.finsecure.payapi.controller;

import com.finsecure.payapi.model.TransactionRequest;
import com.finsecure.payapi.model.TransactionResponse;
import com.finsecure.payapi.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private final ValidationService validationService;

    public TransactionController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("FinSecure Pay API is UP");
    }

    @PostMapping("/validate")
    public ResponseEntity<TransactionResponse> validate(@RequestBody TransactionRequest request) {
        TransactionResponse response = validationService.validateTransaction(request);
        return ResponseEntity.ok(response);
    }
}