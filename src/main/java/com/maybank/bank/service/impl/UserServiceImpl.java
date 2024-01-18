package com.maybank.bank.service.impl;

import com.maybank.bank.dto.AccountInfo;
import com.maybank.bank.dto.BankResponse;
import com.maybank.bank.dto.EmailDetails;
import com.maybank.bank.dto.UserRequest;
import com.maybank.bank.entity.User;
import com.maybank.bank.repository.UserRepo;
import com.maybank.bank.service.EmailService;
import com.maybank.bank.service.UserService;
import com.maybank.bank.util.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepo.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();

        User savedUser = userRepo.save(newUser);

        // send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Mock App Account Creation")
                .messageBody(
                        "Congratulations! Your account has been successfully created." +
                                "\nYour Account Details:" +
                                "\nAccount Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() +
                                "\nAccount Number: " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtil.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }
}
