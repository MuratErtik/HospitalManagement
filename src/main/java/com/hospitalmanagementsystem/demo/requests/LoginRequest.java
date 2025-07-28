package com.hospitalmanagementsystem.demo.requests;

import lombok.Data;

@Data
public class LoginRequest {

    private String tcNo;

    private String password;
}
