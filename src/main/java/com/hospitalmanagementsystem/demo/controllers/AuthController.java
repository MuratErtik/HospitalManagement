package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.exceptions.AuthException;
import com.hospitalmanagementsystem.demo.requests.ChangeMailRequest;
import com.hospitalmanagementsystem.demo.requests.ChangePasswordRequest;
import com.hospitalmanagementsystem.demo.requests.LoginRequest;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import com.hospitalmanagementsystem.demo.responses.ChangeMailResponse;
import com.hospitalmanagementsystem.demo.responses.ChangePasswordResponse;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorScheduleResponse;
import com.hospitalmanagementsystem.demo.responses.SignupResponse;
import com.hospitalmanagementsystem.demo.services.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) throws AuthException, MessagingException {

        String jwt = authService.createUser(signupRequest);

        SignupResponse  signupResponse = new SignupResponse();

        //signupResponse.setUserId(newUSer.getUserId());

        signupResponse.setJwtToken(jwt);

        signupResponse.setMessage("Successfully signed up");

        return ResponseEntity.ok(signupResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignupResponse> signin(@RequestBody LoginRequest loginRequest) throws AuthException, MessagingException {

        String jwt = authService.login(loginRequest);

        SignupResponse  signupResponse = new SignupResponse();

        //signupResponse.setUserId(newUSer.getUserId());

        signupResponse.setJwtToken(jwt);

        signupResponse.setMessage("Successfully sign in.");

        return ResponseEntity.ok(signupResponse);
    }

    @PutMapping("/{userId}/change-mail")
    public ResponseEntity<ChangeMailResponse> changeMail(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long userId,@RequestBody ChangeMailRequest newMail) throws AuthException {


        Long userIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if( userIdFromToken.equals(userId) ){
            ChangeMailResponse response = authService.changeMail(newMail, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestHeader("Authorization") String jwt,
                                                                 @PathVariable Long userId, @RequestBody ChangePasswordRequest request) throws AuthException {


        Long userIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if( userIdFromToken.equals(userId) ){
            ChangePasswordResponse response = authService.changePassword(request, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
