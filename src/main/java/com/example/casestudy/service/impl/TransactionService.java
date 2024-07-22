package com.example.casestudy.service.impl;

import com.example.casestudy.dto.request.TransactionRequest;
import com.example.casestudy.dto.response.MessageResponse;
import com.example.casestudy.dto.response.TransactionResponse;
import com.example.casestudy.entity.Transaction;
import com.example.casestudy.entity.User;
import com.example.casestudy.enums.ETransactionType;
import com.example.casestudy.exception.TransactionException;
import com.example.casestudy.mapper.TransactionMapper;

import com.example.casestudy.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.casestudy.entity.TransactionManagement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService implements TransactionServiceImpl {
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
   private TransactionRepository transactionRepository;

   @Autowired
   private TransactionManagementService transactionManagementService;
    @Transactional
    public TransactionResponse createTransaction(String token,TransactionRequest transactionRequest) {
       User user = userService.getAuthenticatedUserFromToken(token);
        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.getAmount())
                .categoryName(transactionRequest.getCategory())
                .type(ETransactionType.CREATE)
                .createdAt((LocalDateTime.now()))
                .user(user)
                .build();
        transaction = transactionRepository.save(transaction);
       TransactionManagement management = TransactionManagement.builder()
               .userId(user.getId())
               .dateTime(LocalDateTime.now())
               .message(user.getUsername() + " isimli kullanıcı " + transaction.getId().toString() + " li " +
                       transaction.getAmount().toString() + " miktarlı transaction oluşturdu.")
               .build();
     transactionManagementService.saveTransactionManagment(management);
       return transactionMapper.toTransactionResponse(transaction, user);

    }
    @Transactional
    public MessageResponse deleteTransaction(String token,Long transactionId) {
        User user = userService.getAuthenticatedUserFromToken(token);
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new TransactionException("Error: Transaction not found with id " + transactionId));
            transaction.setType(ETransactionType.DELETE);
            transactionRepository.delete(transaction);

        TransactionManagement management = TransactionManagement.builder()
                .dateTime(LocalDateTime.now())
                .userId(user.getId())
                .message(user.getUsername() + " isimli kullanıcı " + transaction.getId().toString() + " li " +
                        transaction.getAmount().toString() + " miktarlı transaction sildi.")
                .build();
        transactionManagementService.saveTransactionManagment(management);
            return new MessageResponse("Transaction with id " + transactionId + " successfully deleted!");
    }
   @Transactional
    public TransactionResponse updateTransaction(String token, Long transactionId, TransactionRequest transactionRequest) {
        User user = userService.getAuthenticatedUserFromToken(token);
        Transaction transaction =  transactionRepository.findByIdAndUser(transactionId, user)
                .orElseThrow(() -> new TransactionException("Error: Transaction not found with id " + transactionId));
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setCategoryName(transactionRequest.getCategory());
        transaction.setUpdatedAt(LocalDateTime.now());
       transaction.setType(ETransactionType.UPDATE);
       transactionRepository.save(transaction);
       TransactionManagement management = TransactionManagement.builder()
               .dateTime(LocalDateTime.now())
               .userId(user.getId())
               .message(user.getUsername() + " isimli kullanıcı " + transaction.getId().toString() + " li " +
                       transaction.getAmount().toString() + " miktarlı transaction güncelledi.")
               .build();
       transactionManagementService.saveTransactionManagment(management);
       return transactionMapper.toTransactionResponse(transaction, user);
    }

    public Map<String, BigDecimal> getUserCategoryTransactionAmounts(String token) {
            User user = userService.getAuthenticatedUserFromToken(token);
            List<Transaction> transactionList = transactionRepository.findByUser(user);
            Map<String, BigDecimal> categoryTotals = new HashMap<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Transaction transaction : transactionList) {
                String category = transaction.getCategoryName();
                BigDecimal amount = transaction.getAmount();
                categoryTotals.put(category, categoryTotals.getOrDefault(category, BigDecimal.ZERO).add(amount));
                totalAmount = totalAmount.add(amount);
            }
            categoryTotals.put("Total", totalAmount);
            return categoryTotals;
    }

      public List<TransactionResponse > getAllTransactionResponse (){
       List <Transaction> transactionList = transactionRepository.findAll();
          List<TransactionResponse> transactionResponses = transactionList.stream().map(transaction -> {
              TransactionResponse response = new TransactionResponse();
              response.setId(transaction.getId());
              response.setUsername(transaction.getUser().getUsername());
              response.setTransactionType(transaction.getType().toString());
              response.setAmount(transaction.getAmount());
              response.setDate(transaction.getDate());
              return response;
          }).collect(Collectors.toList());
          return transactionResponses;
    }
}
