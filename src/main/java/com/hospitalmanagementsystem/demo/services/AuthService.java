package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.entities.UserRole;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRoleRepository;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    //@Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageDigest sha256Digest;


    // maybe user signup already?
    //
    public User createUser(SignupRequest req) {
        User newUser = new User();

        newUser.setTcNo(hashTcNo(req.getTcNo()));
        newUser.setName(req.getName());
        newUser.setLastname(req.getLastname());
        newUser.setEmail(req.getEmail());
//        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setMobileNo(req.getMobileNo());

        UserRole userRole = userRoleRepository.findByUserRole(req.getUserRole());

        newUser.setUserRole(userRole);

        userRepository.save(newUser);

        return newUser;
    }


    private String hashTcNo(String tcNo) {
        byte[] hashed = sha256Digest.digest(tcNo.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashed);
    }
}

