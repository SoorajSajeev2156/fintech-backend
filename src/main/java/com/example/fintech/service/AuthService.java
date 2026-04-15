package com.example.fintech.service;

import com.example.fintech.config.JwtUtil;
import com.example.fintech.dto.LoginRequest;
import com.example.fintech.entity.User;
import com.example.fintech.enums.UserStatus;
import com.example.fintech.exception.ResourceNotFoundException;
import com.example.fintech.exception.UserNotActiveException;
import com.example.fintech.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmailOrMobile(request.getMobileOrEmail(), request.getMobileOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException("User is not active");
        }

        return jwtUtil.generateToken(request.getMobileOrEmail());
    }
}