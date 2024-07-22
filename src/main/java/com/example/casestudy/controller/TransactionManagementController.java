package com.example.casestudy.controller;

import com.example.casestudy.dto.response.TransactionManagementResponse;
import com.example.casestudy.entity.TransactionManagement;
import com.example.casestudy.service.impl.TransactionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/transactions")
public class TransactionManagementController {
    @Autowired
    TransactionManagementService transactionManagementService;
    @GetMapping("/allTransactions")
    public List<TransactionManagementResponse>getAllTransaction()
    {
       return transactionManagementService.getAllTransactionResponse();
    }

    @GetMapping("/{userId}")
    public List<TransactionManagementResponse> getAllTransactionwithtUser(@PathVariable Long userId)
    {
        return transactionManagementService.getAllTransactionResponseUser(userId);
    }

}
