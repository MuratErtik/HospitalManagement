package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.entities.UserRole;
import com.hospitalmanagementsystem.demo.exceptions.AuthException;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRoleRepository;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;



    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageDigest sha256Digest;


    // maybe user signup already?
    //
    public User createUser(SignupRequest req) throws AuthException {

        String hashedTcNo = hashTcNo(req.getTcNo());

        if (userRepository.findByTcNo(hashedTcNo).isPresent()) {
            throw new AuthException("That TC already exists in the system");
        }

        User newUser = new User();

        newUser.setTcNo(hashedTcNo);

        newUser.setName(req.getName());

        newUser.setLastname(req.getLastname());

        newUser.setEmail(req.getEmail());

        newUser.setPassword(passwordEncoder.encode(req.getPassword()));

        newUser.setMobileNo(req.getMobileNo());


        UserRole userRole = userRoleRepository.findByUserRole(req.getUserRole());
        if (userRole == null) {
            throw new AuthException("Invalid user role: " + req.getUserRole());
        }

        newUser.setUserRole(userRole);


        return userRepository.save(newUser);
    }



    private String hashTcNo(String tcNo) {
        byte[] hashed = sha256Digest.digest(tcNo.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashed);
    }
}

