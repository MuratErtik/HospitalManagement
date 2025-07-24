package com.hospitalmanagementsystem.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class SecurityConfig {

    //For password
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    //For tcNo
    @Bean
    public MessageDigest messageDigest() throws NoSuchAlgorithmException {

        return MessageDigest.getInstance("SHA-256");
    }
}
