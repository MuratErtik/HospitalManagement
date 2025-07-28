package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.entities.UserRole;
import com.hospitalmanagementsystem.demo.exceptions.AuthException;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRoleRepository;
import com.hospitalmanagementsystem.demo.requests.LoginRequest;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final JwtProvider jwtProvider;



    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageDigest sha256Digest;

    private final EmailService emailService;

    @Autowired
    private HttpServletRequest request;

    private final AuditLogService auditLogService;



    // maybe user signup already?
    //mail
    //audit log
    public String createUser(SignupRequest req) throws AuthException, MessagingException {

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


        userRepository.save(newUser);

        emailService.afterTheRegisteration(newUser.getEmail(), newUser.getName(), newUser.getLastname());

        auditLogService.logAction(
                newUser.getEmail(),                    // username
                "USER_SIGNUP",                         // action
                "User",                                // entity name
                newUser.getUserId().toString(),            // user ID
                request.getRemoteAddr(),               // IP address
                "User registered with role: " + userRole.getUserRole() // extra details
        );


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                newUser, null, authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);

    }


    public String login(LoginRequest req) throws AuthException {

        String hashedTcNo = hashTcNo(req.getTcNo());

        if (userRepository.findByTcNo(hashedTcNo).isPresent()) {

            User user = userRepository.findByTcNo(hashedTcNo).get();

            if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {

                auditLogService.logAction(
                        user.getEmail(),                    // username
                        "USER_SIGNIN",                         // action
                        "User",                                // entity name
                        user.getUserId().toString(),            // user ID
                        request.getRemoteAddr(),               // IP address
                        "Wrong password: " // extra details
                );
                throw new AuthException("Invalid user password");
            }

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getUserRole().toString()));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null, authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            auditLogService.logAction(
                    user.getEmail(),                    // username
                    "USER_SIGNIN",                         // action
                    "User",                                // entity name
                    user.getUserId().toString(),            // user ID
                    request.getRemoteAddr(),               // IP address
                    "User login with role: " + user.getUserRole() // extra details
            );

            return jwtProvider.generateToken(authentication);


        }
        else{
            auditLogService.logAction(
                    "?",                    // username
                    "USER_LOGIN",                         // action
                    "User",                                // entity name
                    "?",            // user ID
                    request.getRemoteAddr(),               // IP address
                    "Tc No is not in the System." // extra details
            );
            throw new AuthException("User not found");
        }

    }

    private String hashTcNo(String tcNo) {
        byte[] hashed = sha256Digest.digest(tcNo.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashed);
    }
}

