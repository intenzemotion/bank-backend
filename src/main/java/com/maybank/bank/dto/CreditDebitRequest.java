package com.maybank.bank.dto;

import com.maybank.bank.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditDebitRequest {

    private String accountNumber;
    private TransactionType requestType;
    private BigDecimal amount;
}
