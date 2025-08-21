package com.hospitalmanagementsystem.demo.config;

import java.util.*;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;



import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.entities.UserRole;
import com.hospitalmanagementsystem.demo.exceptions.AuthException;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRoleRepository;



import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        String email;
        Long userId = null;
        String userRole = null;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            email = user.getEmail();
            userId = user.getUserId();
            userRole = user.getUserRole().getUserRole();
        } else {
            email = authentication.getName(); // fallback
        }

        return Jwts.builder()
                .setSubject(email)
                .claim("userRole", userRole)
                .claim("userId", userId)
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority) // sadece ROLE_ADMIN, ROLE_DOCTOR
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey())
                .compact();

    }




    public String getEmailFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject(); // "sub" -> email
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userRole", String.class);
    }


//    public String getUserRoleFromToken(String token) {
//        Claims claims = extractAllClaims(token);
//        String roles = (String) claims.get("authorities");
//        String firstRole = roles.split(",")[0].trim();
//
//        UserRole userRole = userRoleRepository.findByUserRole(firstRole);
//        if (userRole == null) {
//            throw new RuntimeException("Role not found for authority: " + firstRole);
//        }
//        return userRole.getUserRole();
//    }

    public String getRoleFromEmail(String email) throws AuthException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AuthException("User not found!");
        }

        return Optional.ofNullable(user.getUserRole())
                .map(UserRole::getUserRole)
                .orElseThrow(() -> new AuthException("Role not assigned!"));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    private Claims extractAllClaims(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
