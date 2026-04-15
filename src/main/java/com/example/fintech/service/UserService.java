package com.example.fintech.service;

import com.example.fintech.dto.RegisterRequest;
import com.example.fintech.dto.VerifyOtpRequest;
import com.example.fintech.entity.Account;
import com.example.fintech.entity.Otp;
import com.example.fintech.entity.User;
import com.example.fintech.enums.UserStatus;
import com.example.fintech.exception.InvalidOtpException;
import com.example.fintech.exception.ResourceNotFoundException;
import com.example.fintech.repository.AccountRepository;
import com.example.fintech.repository.OtpRepository;
import com.example.fintech.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, OtpRepository otpRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.accountRepository = accountRepository;
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setStatus(UserStatus.PENDING);

        userRepository.save(user);

        String generatedOtp = String.valueOf(100000 + new Random().nextInt(900000));

        Otp otp = otpRepository.findByIdentifier(request.getMobile()).orElse(new Otp());
        otp.setIdentifier(request.getMobile());
        otp.setOtpCode(generatedOtp);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        return "User registered successfully. OTP: " + generatedOtp;
    }

    public String verifyOtp(VerifyOtpRequest request) {
        Otp otp = otpRepository.findByIdentifier(request.getMobileOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found"));

        if (!otp.getOtpCode().equals(request.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP expired");
        }

        User user = userRepository.findByEmailOrMobile(request.getMobileOrEmail(), request.getMobileOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        Account account = new Account();
        account.setUser(user);
        account.setBalance(1000.0);
        accountRepository.save(account);

        otpRepository.delete(otp);

        return "OTP verified. User activated and account created with balance 1000";
    }
}