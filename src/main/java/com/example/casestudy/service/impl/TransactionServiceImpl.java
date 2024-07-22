package com.example.casestudy.service.impl;

import com.example.casestudy.dto.request.TransactionRequest;
import com.example.casestudy.dto.response.MessageResponse;
import com.example.casestudy.dto.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.Map;

public interface TransactionServiceImpl {
     TransactionResponse createTransaction(String token, TransactionRequest transactionRequest);
    MessageResponse deleteTransaction(String token, Long transactionId);
    public TransactionResponse updateTransaction(String token, Long transactionId, TransactionRequest transactionRequest);
    Map<String, BigDecimal> getUserCategoryTransactionAmounts(String token);
}
