package com.maybank.bank.service;

import com.maybank.bank.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
