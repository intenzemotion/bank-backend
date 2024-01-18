package com.maybank.bank.service;

import com.maybank.bank.dto.BankResponse;
import com.maybank.bank.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
