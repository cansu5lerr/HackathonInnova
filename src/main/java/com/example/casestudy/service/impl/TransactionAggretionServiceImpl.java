package com.example.casestudy.service.impl;

import com.example.casestudy.entity.Transaction;
import com.example.casestudy.entity.User;
import com.example.casestudy.repository.TransactionRepository;
import com.example.casestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionAggretionServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Scheduled(cron = "0 0 0 * * ?") // daily at midnight
    public void aggregateDailyExpenses() {
        aggregateExpenses(Period.DAILY);
    }

    @Scheduled(cron = "0 0 0 * * MON") // weekly at midnight on Monday
    public void aggregateWeeklyExpenses() {
        aggregateExpenses(Period.WEEKLY);
    }

    @Scheduled(cron = "0 0 0 1 * ?") // monthly at midnight on the first day of month
    public void aggregateMonthlyExpenses() {
        aggregateExpenses(Period.MONTHLY);
    }

    private void aggregateExpenses(Period period) {
        List<User> users = userRepository.findAll();
        LocalDate now = LocalDate.now();

        for (User user : users) {
            LocalDate startDate = getStartDate(now, period);
            List<Transaction> transactionList = transactionRepository.findByUserAndDateBetween(user, startDate, now);
            BigDecimal totalAmount = transactionList.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("User: " + user.getUsername() + ", Period: " + period + ", Total Amount: " + totalAmount);
        }
    }

    private LocalDate getStartDate(LocalDate now, Period period) {
        switch (period) {
            case DAILY:
                return now.minusDays(1);
            case WEEKLY:
                return now.minusWeeks(1);
            case MONTHLY:
                return now.minusMonths(1);
            default:
                throw new IllegalArgumentException("Unknown period: " + period);
        }
    }
    enum Period {
        DAILY, WEEKLY, MONTHLY
    }
}

