package com.maybank.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password; // will be encrypted when transferring over network
    private String address;
    private String phoneNumber;
}
