package com.maybank.bank.service;

import com.maybank.bank.dto.TransactionResponse;

public interface TransactionService {

    void saveTransaction(TransactionResponse transactionResponse);
}
