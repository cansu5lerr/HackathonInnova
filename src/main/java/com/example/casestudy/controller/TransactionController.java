package com.example.casestudy.controller;

import com.example.casestudy.dto.request.TransactionRequest;
import com.example.casestudy.dto.response.TransactionResponse;
import com.example.casestudy.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @PostMapping("/addTransaction")
    public ResponseEntity<TransactionResponse> createTransaction(
           @RequestHeader("Authorization") String token,
           @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse createdTransaction = transactionService.createTransaction(token, transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @RequestHeader("Authorization") String token,
            @PathVariable Long transactionId,
            @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse updatedTransaction = transactionService.updateTransaction(token, transactionId, transactionRequest);
        return ResponseEntity.ok(updatedTransaction);
    }
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(
            @RequestHeader("Authorization") String token,
            @PathVariable Long transactionId) {
        transactionService.deleteTransaction(token,transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @GetMapping("/totalAmounts")
    public ResponseEntity<Map<String, BigDecimal>> getUserTransactionAmounts(
            @RequestHeader("Authorization") String token)
            {
        try {
            Map<String, BigDecimal> categoryTotals = transactionService.getUserCategoryTransactionAmounts(token);
            return ResponseEntity.ok().body(categoryTotals);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // User not found
        }
    }



}
