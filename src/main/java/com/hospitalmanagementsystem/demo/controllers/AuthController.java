package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import com.hospitalmanagementsystem.demo.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {

        User newUSer = authService.createUser(signupRequest);

        return ResponseEntity.ok(newUSer);
    }
}
