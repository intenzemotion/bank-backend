package com.maybank.bank.service.impl;

import com.maybank.bank.dto.TransactionResponse;
import com.maybank.bank.entity.Transaction;
import com.maybank.bank.repository.TransactionRepo;
import com.maybank.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionServiceImpl(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void saveTransaction(TransactionResponse transactionResponse) {
        Transaction transaction = Transaction.builder()
                .accountNumber(transactionResponse.getAccountNumber())
                .transactionType(transactionResponse.getTransactionType())
                .amount(transactionResponse.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepo.save(transaction);
    }
}
