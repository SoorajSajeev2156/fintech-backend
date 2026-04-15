package com.example.fintech.controller;

import com.example.fintech.dto.ApiResponse;
import com.example.fintech.dto.LoginRequest;
import com.example.fintech.dto.LoginResponse;
import com.example.fintech.dto.RegisterRequest;
import com.example.fintech.dto.VerifyOtpRequest;
import com.example.fintech.service.AuthService;
import com.example.fintech.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequest request) {
        return new ApiResponse(userService.register(request));
    }

    @PostMapping("/verify-otp")
    public ApiResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return new ApiResponse(userService.verifyOtp(request));
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return new LoginResponse(authService.login(request));
    }
}