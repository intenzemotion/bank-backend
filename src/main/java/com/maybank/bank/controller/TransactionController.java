package com.maybank.bank.controller;

import com.maybank.bank.entity.Transaction;
import com.maybank.bank.service.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bankStatement")
public class TransactionController {

    private final BankStatement bankStatement;

    @Autowired
    public TransactionController(BankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
