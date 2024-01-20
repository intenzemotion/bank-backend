package com.maybank.bank.util;

import java.time.Year;

public class AccountUtil {

    /**
     * current year + random 6 digits
     */
    public static String generateAccountNumber() {

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // generate random number between min and max
        int random = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // convert the current year and randNumber to strings, then concatenate
        String year = String.valueOf(currentYear);
        String rand = String.valueOf(random);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(rand).toString();
    }

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account has been successfully created!";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the account number does not exist.";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User account found!";

    public static final String INSUFFICIENT_BALANCE_CODE = "005";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "User account has insufficient balance!";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "006";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account credited successfully!";

    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User account debited successfully!";

    public static final String SOURCE_ACCOUNT_NOT_EXIST_CODE = "008";
    public static final String SOURCE_ACCOUNT_NOT_EXIST_MESSAGE = "User with the account number does not exist.";

    public static final String DESTINATION_ACCOUNT_NOT_EXIST_CODE = "009";
    public static final String DESTINATION_ACCOUNT_NOT_EXIST_MESSAGE = "User with the account number does not exist.";

    public static final String TRANSFER_SUCCESS_CODE = "010";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer successful.";

    public static final String LOGIN_SUCCESS_CODE = "011";
    public static final String LOGIN_SUCCESS_MESSAGE = "Login success.";
}
