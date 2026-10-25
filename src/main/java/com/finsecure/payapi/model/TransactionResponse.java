// TransactionResponse.java
package com.finsecure.payapi.model;

public class TransactionResponse {
    private String transactionId;
    private String status; // APPROVED or REJECTED
    private String message;

    public TransactionResponse(String transactionId, String status, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}