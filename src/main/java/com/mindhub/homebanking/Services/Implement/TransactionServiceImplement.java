package com.mindhub.homebanking.Services.Implement;


import com.mindhub.homebanking.DTO.TransactionDTO;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).get();
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccountAndDate(Account account, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findAllByAccountAndDateBetween(account, start, end);
    }


    @Override
    public List<Transaction> getAllTransactionsByAccount(Account account) {
        return transactionRepository.findAllByAccount(account);
    }
}
