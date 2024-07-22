package com.example.casestudy.service.impl;

import com.example.casestudy.dto.response.TransactionManagementResponse;
import com.example.casestudy.entity.TransactionManagement;

import java.util.List;

public interface TransactionManagementServiceImpl {
    List<TransactionManagementResponse> getAllTransactionResponse ();
    List<TransactionManagementResponse> getAllTransactionResponseUser(Long userId);
    public void saveTransactionManagment(TransactionManagement transactionManagement);
}
