package com.pm.auth_service.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String email,String role){
        return Jwts.builder()
        .subject(email)
        .claim("role",role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis()+1000 * 60 * 60 * 10))
        .signWith(secretKey)
        .compact();
    }

}
