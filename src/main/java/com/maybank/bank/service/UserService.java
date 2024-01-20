package com.maybank.bank.service;

import com.maybank.bank.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditRequest);

    BankResponse debitAccount(CreditDebitRequest debitRequest);

    BankResponse transfer(TransferRequest transferRequest);

    BankResponse login(LoginDetails loginDetails);
}
