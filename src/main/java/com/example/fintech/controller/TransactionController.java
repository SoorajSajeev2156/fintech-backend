package com.example.fintech.controller;

import com.example.fintech.dto.ApiResponse;
import com.example.fintech.dto.TransferRequest;
import com.example.fintech.entity.Transaction;
import com.example.fintech.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ApiResponse transfer(@Valid @RequestBody TransferRequest request) {
        return new ApiResponse(transactionService.transferMoney(request));
    }

    @GetMapping("/transactions/{userId}")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        return transactionService.getTransactions(userId);
    }
}