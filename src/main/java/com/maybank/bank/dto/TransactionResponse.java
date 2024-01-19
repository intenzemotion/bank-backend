package com.maybank.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private String status; // pending, failure, success
}
