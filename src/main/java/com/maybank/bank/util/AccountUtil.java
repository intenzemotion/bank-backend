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

    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
}
