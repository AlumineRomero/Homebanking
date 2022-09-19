package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.DTO.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    public List<Transaction> getAllTransactions();
    public Transaction getTransactionById(Long id);
    public void saveTransaction(Transaction transaction);
    public List<Transaction> getTransactionsByAccountAndDate (Account account, LocalDateTime start, LocalDateTime end);
    public List<Transaction> getAllTransactionsByAccount (Account account);


}
