package com.example.casestudy.repository;

import com.example.casestudy.entity.Transaction;
import com.example.casestudy.entity.TransactionManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionManagementRepository  extends JpaRepository<TransactionManagement, Long> {
    List<TransactionManagement> findByUserIdOrderByDateTime(Long userId);

}
