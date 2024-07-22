package com.example.casestudy.entity;


import com.example.casestudy.enums.ERole;
import com.example.casestudy.enums.ETransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="amount")
    private BigDecimal amount;


    @Column(name="categoryName")
    private String categoryName;

    @Column(name="createdTime")
    private LocalDateTime createdAt;

    @Column(name="updatedTime")
    private LocalDateTime updatedAt;

    @Column(name="date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETransactionType type;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}