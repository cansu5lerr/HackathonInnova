package com.example.casestudy.repository;

import com.example.casestudy.entity.Transaction;
import com.example.casestudy.entity.TransactionManagement;
import com.example.casestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    Optional<Transaction> findByIdAndUser(Long transactionId, User user);
    List<Transaction> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

}