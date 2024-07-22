package com.example.casestudy.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransactionRequest {
    @NotBlank(message = "Amount cannot be blank")
    private BigDecimal amount;
    @NotBlank(message = "Category cannot be blank")
    private String category;
}
