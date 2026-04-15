package com.example.fintech.service;

import com.example.fintech.dto.TransferRequest;
import com.example.fintech.entity.Account;
import com.example.fintech.entity.Transaction;
import com.example.fintech.entity.User;
import com.example.fintech.exception.InsufficientBalanceException;
import com.example.fintech.exception.ResourceNotFoundException;
import com.example.fintech.repository.AccountRepository;
import com.example.fintech.repository.TransactionRepository;
import com.example.fintech.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository,
                              AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String transferMoney(TransferRequest request) {
        if (request.getSenderUserId().equals(request.getReceiverUserId())) {
            throw new RuntimeException("Sender and receiver cannot be same");
        }

        User sender = userRepository.findById(request.getSenderUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Account senderAccount = accountRepository.findByUser(sender)
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        Account receiverAccount = accountRepository.findByUser(receiver)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        if (senderAccount.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        senderAccount.setBalance(senderAccount.getBalance() - request.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + request.getAmount());

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction transaction = new Transaction();
        transaction.setSenderUserId(sender.getId());
        transaction.setReceiverUserId(receiver.getId());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionTime(LocalDateTime.now());

        transactionRepository.save(transaction);
                

        transactionRepository.save(transaction);

        return "Transfer successful";
    }

    public List<Transaction> getTransactions(Long userId) {
        return transactionRepository.findBySenderUserIdOrReceiverUserId(userId, userId);
    }
}