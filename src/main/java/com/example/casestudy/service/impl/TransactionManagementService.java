package com.example.casestudy.service.impl;
import com.example.casestudy.dto.response.TransactionManagementResponse;
import com.example.casestudy.entity.TransactionManagement;
import com.example.casestudy.repository.TransactionManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionManagementService implements TransactionManagementServiceImpl {

    @Autowired
    TransactionManagementRepository transactionManagementRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public List<TransactionManagementResponse> getAllTransactionResponse (){
        List <TransactionManagement> transactionList = transactionManagementRepository.findAll();
        List<TransactionManagementResponse> transactionResponses = transactionList.stream().map(transaction -> {
            TransactionManagementResponse response = new TransactionManagementResponse();
            response.setMessage(transaction.getMessage());
            response.setDateTime(transaction.getDateTime());
            return response;
        }).collect(Collectors.toList());
        return transactionResponses;
    }

    public List<TransactionManagementResponse> getAllTransactionResponseUser(Long userId) {

        List<TransactionManagement> transactionList = transactionManagementRepository.findByUserIdOrderByDateTime(userId);
        return transactionList.stream().map(transaction -> {
            TransactionManagementResponse response = new TransactionManagementResponse();
            response.setMessage(transaction.getMessage());
            response.setDateTime(transaction.getDateTime());
            return response;
        }).collect(Collectors.toList());
    }

    public void saveTransactionManagment(TransactionManagement transactionManagement) {
        transactionManagementRepository.save(transactionManagement);
    }
}
