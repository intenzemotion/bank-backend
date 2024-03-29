package com.maybank.bank.service.impl;

import com.maybank.bank.config.JwtTokenProvider;
import com.maybank.bank.dto.*;
import com.maybank.bank.entity.User;
import com.maybank.bank.enumeration.Role;
import com.maybank.bank.enumeration.TransactionType;
import com.maybank.bank.repository.UserRepo;
import com.maybank.bank.service.EmailService;
import com.maybank.bank.service.TransactionService;
import com.maybank.bank.service.UserService;
import com.maybank.bank.util.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, EmailService emailService, TransactionService transactionService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepo.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_EXISTS_MESSAGE)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword())) // encode immediately
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .role(Role.USER)
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
                .responseCode(AccountUtil.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }

    public BankResponse login(LoginDetails loginDetails) {
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword())
        );

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You're logged in!")
                .recipient(loginDetails.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, please contact your bank.")
                .build();
        emailService.sendEmailAlert(loginAlert);

        return BankResponse.builder()
                .responseCode(AccountUtil.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtil.LOGIN_SUCCESS_MESSAGE)
                .token(jwtTokenProvider.generateToken(authentication))
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtil.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE;
        }

        User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditRequest) {

        // check if account number exist
        boolean isAccountExist = userRepo.existsByAccountNumber(creditRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        User userToCredit = userRepo.findByAccountNumber(creditRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));
        userRepo.save(userToCredit);

        // save transaction
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType(String.valueOf(TransactionType.CREDIT))
                .amount(creditRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(transactionResponse);

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest debitRequest) {

        // check if account number exist
        boolean isAccountExist = userRepo.existsByAccountNumber(debitRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        User userToDebit = userRepo.findByAccountNumber(debitRequest.getAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(debitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));
            userRepo.save(userToDebit);

            // save transaction
            TransactionResponse transactionResponse = TransactionResponse.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType(String.valueOf(TransactionType.DEBIT))
                    .amount(debitRequest.getAmount())
                    .status("SUCCESS")
                    .build();
            transactionService.saveTransaction(transactionResponse);

            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtil.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(debitRequest.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {

        // debit
        boolean isDestinationAccountExist = userRepo.existsByAccountNumber(transferRequest.getSourceAccountNumber());

        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.DESTINATION_ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.DESTINATION_ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        User sourceAccountUser = userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if (sourceAccountUser.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
        String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName();
        userRepo.save(sourceAccountUser);

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Debit Alert")
                .recipient(sourceAccountUser.getEmail())
                .messageBody(
                        "The sum of " + transferRequest.getAmount() + " has been deducted from your account." +
                                "\nYour current balance is " + sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        // save debit transaction
        TransactionResponse transactionDebitResponse = TransactionResponse.builder()
                .accountNumber(sourceAccountUser.getAccountNumber())
                .transactionType(String.valueOf(TransactionType.DEBIT))
                .amount(transferRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(transactionDebitResponse);

        // credit
        User destinationAccountUser = userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
        String destinationUsername = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName();
        userRepo.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Credit Alert")
                .recipient(destinationAccountUser.getEmail())
                .messageBody(
                        "The sum of " + transferRequest.getAmount() + " has been sent to your account." +
                                "\nYour current balance is " + destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        // save credit transaction
        TransactionResponse transactionCreditResponse = TransactionResponse.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType(String.valueOf(TransactionType.CREDIT))
                .amount(transferRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(transactionCreditResponse);

        return BankResponse.builder()
                .responseCode(AccountUtil.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtil.TRANSFER_SUCCESS_MESSAGE)
                .build();
    }
}
