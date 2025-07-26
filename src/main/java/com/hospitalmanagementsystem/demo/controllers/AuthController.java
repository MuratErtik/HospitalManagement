package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.exceptions.AuthException;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import com.hospitalmanagementsystem.demo.responses.SignupResponse;
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
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) throws AuthException {

        String jwt = authService.createUser(signupRequest);

        SignupResponse  signupResponse = new SignupResponse();

        //signupResponse.setUserId(newUSer.getUserId());

        signupResponse.setJwtToken(jwt);

        signupResponse.setMessage("Successfully signed up");

        return ResponseEntity.ok(signupResponse);
    }
}
