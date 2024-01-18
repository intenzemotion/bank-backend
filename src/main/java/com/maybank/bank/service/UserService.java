package com.maybank.bank.service;

import com.maybank.bank.dto.BankResponse;
import com.maybank.bank.dto.CreditDebitRequest;
import com.maybank.bank.dto.EnquiryRequest;
import com.maybank.bank.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditRequest);

    BankResponse debitAccount(CreditDebitRequest debitRequest);
}
