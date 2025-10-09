package com.pm.auth_service.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pm.auth_service.dto.LoginRequestDTO;
import com.pm.auth_service.util.JwtUtil;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO){
        Optional<String> token = userService
        .findByEmail(loginRequestDTO.getEmail())
        .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
        u.getPassword()))
        .map(u -> jwtUtil.generateToken(u.getEmail(),u.getRole()));

        return token;
    }
}
