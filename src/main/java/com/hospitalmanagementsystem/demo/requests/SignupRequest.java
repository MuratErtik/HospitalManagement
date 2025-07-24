package com.hospitalmanagementsystem.demo.requests;

import lombok.Data;

@Data
public class SignupRequest {

    private String tcNo;

    private String name;

    private String lastname;

    private String email;

    private String password;

    private String  mobileNo;

    private String userRole;
}
